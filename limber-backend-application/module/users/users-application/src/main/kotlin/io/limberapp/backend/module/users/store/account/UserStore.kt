package io.limberapp.backend.module.users.store.account

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isUniqueConstraintViolation
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.users.exception.account.EmailAddressAlreadyTaken
import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.model.account.UserModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

private const val EMAIL_ADDRESS_UNIQUE_CONSTRAINT = "user_lower_idx"

internal class UserStore @Inject constructor(private val jdbi: Jdbi) : SqlStore() {
  fun create(model: UserModel): UserModel {
    return jdbi.withHandle<UserModel, Exception> {
      try {
        it.createQuery(sqlResource("create"))
          .bindKotlin(model)
          .mapTo(UserModel::class.java)
          .single()
      } catch (e: UnableToExecuteStatementException) {
        handleCreateError(e)
      }
    }
  }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    if (error.isUniqueConstraintViolation(EMAIL_ADDRESS_UNIQUE_CONSTRAINT)) throw EmailAddressAlreadyTaken()
    throw e
  }

  fun get(userGuid: UUID): UserModel? {
    return jdbi.withHandle<UserModel?, Exception> {
      it.createQuery("SELECT * FROM users.user WHERE guid = :guid AND archived_date IS NULL")
        .bind("guid", userGuid)
        .mapTo(UserModel::class.java)
        .singleNullOrThrow()
    }
  }

  fun getByEmailAddress(emailAddress: String): UserModel? {
    return jdbi.withHandle<UserModel?, Exception> {
      it.createQuery(
          """
                    SELECT *
                    FROM users.user
                    WHERE LOWER(email_address) = LOWER(:emailAddress)
                      AND archived_date IS NULL
                    """.trimIndent()
        )
        .bind("emailAddress", emailAddress)
        .mapTo(UserModel::class.java)
        .singleNullOrThrow()
    }
  }

  fun getByOrgGuid(orgGuid: UUID): Set<UserModel> {
    return jdbi.withHandle<Set<UserModel>, Exception> {
      it.createQuery(
          """
                    SELECT *
                    FROM users.user
                    WHERE org_guid = :orgGuid
                      AND archived_date IS NULL
                    """.trimIndent()
        )
        .bind("orgGuid", orgGuid)
        .mapTo(UserModel::class.java)
        .toSet()
    }
  }

  fun update(userGuid: UUID, update: UserModel.Update): UserModel {
    return jdbi.inTransaction<UserModel, Exception> {
      val updateCount = it.createUpdate(sqlResource("update"))
        .bind("guid", userGuid)
        .bindKotlin(update)
        .execute()
      return@inTransaction when (updateCount) {
        0 -> throw UserNotFound()
        1 -> checkNotNull(get(userGuid))
        else -> badSql()
      }
    }
  }

  fun delete(userGuid: UUID) {
    jdbi.useTransaction<Exception> {
      val updateCount = it.createUpdate(
          """
                    UPDATE users.user
                    SET archived_date = NOW()
                    WHERE guid = :guid
                      AND archived_date IS NULL
                    """.trimIndent()
        )
        .bind("guid", userGuid)
        .execute()
      return@useTransaction when (updateCount) {
        0 -> throw UserNotFound()
        1 -> Unit
        else -> badSql()
      }
    }
  }
}

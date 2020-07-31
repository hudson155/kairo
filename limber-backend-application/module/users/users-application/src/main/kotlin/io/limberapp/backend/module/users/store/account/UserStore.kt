package io.limberapp.backend.module.users.store.account

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isUniqueConstraintViolation
import io.limberapp.backend.module.users.exception.account.EmailAddressAlreadyTaken
import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.model.account.UserModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

private const val EMAIL_ADDRESS_UNIQUE_CONSTRAINT = "user_org_guid_lower_idx"

internal class UserStore @Inject constructor(private val jdbi: Jdbi) : SqlStore(jdbi) {
  fun create(model: UserModel): UserModel {
    return jdbi.withHandle<UserModel, Exception> {
      try {
        it.createQuery(sqlResource("/store/user/create.sql"))
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

  fun get(orgGuid: UUID? = null, userGuid: UUID? = null, emailAddress: String? = null): List<UserModel> {
    return jdbi.withHandle<List<UserModel>, Exception> {
      it.createQuery("SELECT * FROM users.user WHERE <conditions> AND archived_date IS NULL").build {
        if (orgGuid != null) {
          conditions.add("org_guid = :orgGuid")
          bindings["orgGuid"] = orgGuid
        }
        if (userGuid != null) {
          conditions.add("guid = :userGuid")
          bindings["userGuid"] = userGuid
        }
        if (emailAddress != null) {
          conditions.add("email_address = :emailAddress")
          bindings["emailAddress"] = emailAddress
        }
      }
        .mapTo(UserModel::class.java)
        .list()
    }
  }

  fun update(userGuid: UUID, update: UserModel.Update): UserModel {
    return jdbi.inTransaction<UserModel, Exception> {
      val updateCount = it.createUpdate(sqlResource("/store/user/update.sql"))
        .bind("guid", userGuid)
        .bindKotlin(update)
        .execute()
      return@inTransaction when (updateCount) {
        0 -> throw UserNotFound()
        1 -> get(userGuid = userGuid).single()
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

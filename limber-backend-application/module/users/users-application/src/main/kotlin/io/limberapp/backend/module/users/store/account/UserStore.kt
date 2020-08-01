package io.limberapp.backend.module.users.store.account

import com.google.inject.Inject
import com.google.inject.Singleton
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

@Singleton
internal class UserStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi) {
  fun create(model: UserModel): UserModel =
    withHandle { handle ->
      return@withHandle try {
        handle.createQuery(sqlResource("/store/user/create.sql"))
          .bindKotlin(model)
          .mapTo(UserModel::class.java)
          .one()
      } catch (e: UnableToExecuteStatementException) {
        handleCreateError(e)
      }
    }

  fun get(userGuid: UUID): UserModel? =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/user/get.sql"))
        .bind("userGuid", userGuid)
        .mapTo(UserModel::class.java)
        .findOne().orElse(null)
    }

  fun getByOrgGuidAndEmailAddress(orgGuid: UUID, emailAddress: String): UserModel? =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/user/getByOrgGuidAndEmailAddress.sql"))
        .bind("orgGuid", orgGuid)
        .bind("emailAddress", emailAddress)
        .mapTo(UserModel::class.java)
        .findOne().orElse(null)
    }

  fun getByOrgGuid(orgGuid: UUID): Set<UserModel> =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/user/getByOrgGuid.sql"))
        .bind("orgGuid", orgGuid)
        .mapTo(UserModel::class.java)
        .toSet()
    }

  fun update(userGuid: UUID, update: UserModel.Update): UserModel =
    inTransaction { handle ->
      val updateCount = handle.createUpdate(sqlResource("/store/user/update.sql"))
        .bind("userGuid", userGuid)
        .bindKotlin(update)
        .execute()
      return@inTransaction when (updateCount) {
        0 -> throw UserNotFound()
        1 -> checkNotNull(get(userGuid))
        else -> badSql()
      }
    }

  fun delete(userGuid: UUID) =
    inTransaction { handle ->
      val updateCount = handle.createUpdate(sqlResource("/store/user/delete.sql"))
        .bind("userGuid", userGuid)
        .execute()
      return@inTransaction when (updateCount) {
        0 -> throw UserNotFound()
        1 -> Unit
        else -> badSql()
      }
    }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isUniqueConstraintViolation(EMAIL_ADDRESS_UNIQUE_CONSTRAINT) -> throw EmailAddressAlreadyTaken()
      else -> throw e
    }
  }
}

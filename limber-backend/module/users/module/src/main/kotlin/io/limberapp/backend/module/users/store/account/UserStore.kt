package io.limberapp.backend.module.users.store.account

import com.google.inject.Inject
import com.google.inject.Singleton
import io.limberapp.backend.module.users.exception.account.EmailAddressAlreadyTaken
import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.model.account.UserModel
import io.limberapp.common.store.SqlStore
import io.limberapp.common.store.isUniqueConstraintViolation
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

private const val UNIQ_EMAIL_ADDRESS = "uniq__user__email_address"

@Singleton
internal class UserStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi) {
  fun create(model: UserModel): UserModel =
      withHandle { handle ->
        try {
          handle.createQuery(sqlResource("/store/user/create.sql"))
              .bindKotlin(model)
              .mapTo(UserModel::class.java)
              .single()
        } catch (e: UnableToExecuteStatementException) {
          handleCreateError(e)
        }
      }

  fun get(userGuid: UUID): UserModel? =
      withHandle { handle ->
        handle.createQuery(sqlResource("/store/user/get.sql"))
            .bind("userGuid", userGuid)
            .mapTo(UserModel::class.java)
            .singleOrNull()
      }

  fun getByEmailAddress(orgGuid: UUID, emailAddress: String): UserModel? =
      withHandle { handle ->
        handle.createQuery(sqlResource("/store/user/getByEmailAddress.sql"))
            .bind("orgGuid", orgGuid)
            .bind("emailAddress", emailAddress)
            .mapTo(UserModel::class.java)
            .singleOrNull()
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
        handle.createQuery(sqlResource("/store/user/update.sql"))
            .bind("userGuid", userGuid)
            .bindKotlin(update)
            .mapTo(UserModel::class.java)
            .singleNullOrThrow() ?: throw UserNotFound()
      }

  fun delete(userGuid: UUID): Unit =
      inTransaction { handle ->
        handle.createUpdate(sqlResource("/store/user/delete.sql"))
            .bind("userGuid", userGuid)
            .updateOnly() ?: throw UserNotFound()
      }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isUniqueConstraintViolation(UNIQ_EMAIL_ADDRESS) -> throw EmailAddressAlreadyTaken()
      else -> throw e
    }
  }
}

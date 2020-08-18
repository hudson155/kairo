package io.limberapp.backend.module.users.store.account

import com.google.inject.Inject
import com.google.inject.Singleton
import com.piperframework.finder.Finder
import com.piperframework.store.SqlStore
import com.piperframework.store.isUniqueConstraintViolation
import com.piperframework.store.withFinder
import io.limberapp.backend.module.users.exception.account.EmailAddressAlreadyTaken
import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.model.account.UserFinder
import io.limberapp.backend.module.users.model.account.UserModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

private const val UNIQ_EMAIL_ADDRESS = "uniq__user__email_address"

@Singleton
internal class UserStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi), Finder<UserModel, UserFinder> {
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

  override fun <R> find(result: (Iterable<UserModel>) -> R, query: UserFinder.() -> Unit): R =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/user/find.sql"))
        .withFinder(UserQueryBuilder().apply(query))
        .mapTo(UserModel::class.java)
        .let(result)
    }

  fun update(userGuid: UUID, update: UserModel.Update): UserModel =
    inTransaction { handle ->
      val updateCount = handle.createUpdate(sqlResource("/store/user/update.sql"))
        .bind("userGuid", userGuid)
        .bindKotlin(update)
        .execute()
      return@inTransaction when (updateCount) {
        0 -> throw UserNotFound()
        1 -> findOnlyOrThrow { userGuid(userGuid) }
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
      error.isUniqueConstraintViolation(UNIQ_EMAIL_ADDRESS) -> throw EmailAddressAlreadyTaken()
      else -> throw e
    }
  }
}

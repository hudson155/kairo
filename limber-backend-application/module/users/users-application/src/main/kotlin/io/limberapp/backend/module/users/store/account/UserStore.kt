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
import org.jetbrains.exposed.sql.Database
import java.util.UUID

private const val EMAIL_ADDRESS_UNIQUE_CONSTRAINT = "user_email_address_key"

internal class UserStore @Inject constructor(
    database: Database,
    private val jdbi: Jdbi
) : SqlStore(database) {
    fun create(model: UserModel): Unit = jdbi.useHandle<Exception> {
        try {
            it.createUpdate(sqlResource(this::create.name)).bindKotlin(model).execute()
        } catch (e: UnableToExecuteStatementException) {
            val error = e.serverErrorMessage ?: throw e
            when {
                error.isUniqueConstraintViolation(EMAIL_ADDRESS_UNIQUE_CONSTRAINT) ->
                    throw EmailAddressAlreadyTaken(model.emailAddress)
                else -> throw e
            }
        }
    }

    fun get(userGuid: UUID) = jdbi.withHandle<UserModel?, Exception> {
        it.createQuery("SELECT * FROM users.user WHERE guid = :guid")
            .bind("guid", userGuid)
            .mapTo(UserModel::class.java)
            .singleNullOrThrow()
    }

    fun getByEmailAddress(emailAddress: String) = jdbi.withHandle<UserModel?, Exception> {
        it.createQuery("SELECT * FROM users.user WHERE email_address = :emailAddress")
            .bind("emailAddress", emailAddress)
            .mapTo(UserModel::class.java)
            .singleNullOrThrow()
    }

    fun update(userGuid: UUID, update: UserModel.Update): UserModel {
        return jdbi.inTransaction<UserModel, Exception> {
            val updateCount = it.createUpdate(sqlResource(this::update.name))
                .bind("guid", userGuid)
                .bindKotlin(update)
                .execute()

            when (updateCount) {
                0 -> throw UserNotFound()
                1 -> return@inTransaction checkNotNull(get(userGuid))
                else -> badSql()
            }
        }
    }

    fun delete(userGuid: UUID) = jdbi.useTransaction<Exception> {
        val updateCount = it.createUpdate("DELETE FROM users.user WHERE guid = :guid")
            .bind("guid", userGuid)
            .execute()

        when (updateCount) {
            0 -> throw UserNotFound()
            1 -> return@useTransaction
            else -> badSql()
        }
    }
}

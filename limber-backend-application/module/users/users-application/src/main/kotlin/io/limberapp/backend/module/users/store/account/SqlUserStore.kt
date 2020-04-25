package io.limberapp.backend.module.users.store.account

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isUniqueConstraintViolation
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.users.entity.account.UserTable
import io.limberapp.backend.module.users.exception.account.EmailAddressAlreadyTaken
import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.model.account.UserModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.util.UUID

internal class SqlUserStore @Inject constructor(
    database: Database,
    private val sqlAccountMapper: SqlAccountMapperImpl
) : UserStore, SqlStore(database) {
    override fun create(model: UserModel) = transaction {
        doOperation { UserTable.insert { sqlAccountMapper.userEntity(it, model) } } andHandleError {
            when {
                error.isUniqueConstraintViolation(UserTable.emailAddressUniqueConstraint) ->
                    throw EmailAddressAlreadyTaken(model.emailAddress)
            }
        }
    }

    override fun get(userGuid: UUID) = transaction {
        val entity = UserTable
            .select { UserTable.guid eq userGuid }
            .singleNullOrThrow() ?: return@transaction null
        return@transaction sqlAccountMapper.userModel(entity)
    }

    override fun getByEmailAddress(emailAddress: String) = transaction {
        val entity = UserTable
            .select { UserTable.emailAddress eq emailAddress }
            .singleNullOrThrow() ?: return@transaction null
        return@transaction sqlAccountMapper.userModel(entity)
    }

    override fun update(userGuid: UUID, update: UserModel.Update) = transaction {
        UserTable
            .updateExactlyOne(
                where = { UserTable.guid eq userGuid },
                body = { sqlAccountMapper.userEntity(it, update) },
                notFound = { throw UserNotFound() }
            )
        return@transaction checkNotNull(get(userGuid))
    }

    override fun delete(userGuid: UUID) = transaction<Unit> {
        UserTable.deleteExactlyOne(where = { UserTable.guid eq userGuid }, notFound = { throw UserNotFound() })
    }
}

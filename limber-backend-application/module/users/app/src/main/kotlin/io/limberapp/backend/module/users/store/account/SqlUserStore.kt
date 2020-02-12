package io.limberapp.backend.module.users.store.account

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isUniqueConstraintViolation
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.entity.account.AccountTable
import io.limberapp.backend.module.users.entity.account.UserTable
import io.limberapp.backend.module.users.exception.account.EmailAddressAlreadyTaken
import io.limberapp.backend.module.users.exception.account.UserAlreadyHasRole
import io.limberapp.backend.module.users.exception.account.UserDoesNotHaveRole
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
        AccountTable.insert { sqlAccountMapper.accountEntity(it, model) }
        doOperation { UserTable.insert { sqlAccountMapper.userEntity(it, model) } } andHandleError {
            when {
                error.isUniqueConstraintViolation(UserTable.emailAddressUniqueConstraint) ->
                    throw EmailAddressAlreadyTaken(model.emailAddress)
            }
        }
    }

    override fun get(userId: UUID) = transaction {
        val entity = (UserTable innerJoin AccountTable)
            .select { AccountTable.guid eq userId }
            .singleOrNull() ?: return@transaction null
        return@transaction sqlAccountMapper.userModel(entity)
    }

    override fun getByEmailAddress(emailAddress: String) = transaction {
        val entity = (UserTable innerJoin AccountTable)
            .select { UserTable.emailAddress eq emailAddress }
            .singleOrNull() ?: return@transaction null
        return@transaction sqlAccountMapper.userModel(entity)
    }

    override fun update(userId: UUID, update: UserModel.Update) = transaction {
        UserTable
            .updateExactlyOne(
                where = { UserTable.accountGuid eq userId },
                body = { sqlAccountMapper.userEntity(it, update) },
                notFound = { throw UserNotFound() }
            )
        return@transaction checkNotNull(get(userId))
    }

    override fun addRole(userId: UUID, role: JwtRole) = transaction {
        get(userId)?.let { if (role in it.roles) throw UserAlreadyHasRole(role) }
        AccountTable
            .updateExactlyOne(
                where = { AccountTable.guid eq userId },
                body = {
                    sqlAccountMapper.accountEntity(
                        updateStatement = it,
                        identityProvider = if (role == JwtRole.IDENTITY_PROVIDER) true else null,
                        superuser = if (role == JwtRole.SUPERUSER) true else null
                    )
                },
                notFound = { throw UserNotFound() }
            )
        return@transaction checkNotNull(get(userId))
    }

    override fun removeRole(userId: UUID, role: JwtRole) = transaction {
        get(userId)?.let { if (role !in it.roles) throw UserDoesNotHaveRole(role) }
        AccountTable
            .updateExactlyOne(
                where = { AccountTable.guid eq userId },
                body = {
                    sqlAccountMapper.accountEntity(
                        updateStatement = it,
                        identityProvider = if (role == JwtRole.IDENTITY_PROVIDER) false else null,
                        superuser = if (role == JwtRole.SUPERUSER) false else null
                    )
                },
                notFound = { throw UserNotFound() }
            )
        return@transaction checkNotNull(get(userId))
    }

    override fun delete(userId: UUID) = transaction<Unit> {
        AccountTable.deleteExactlyOne(where = { AccountTable.guid eq userId }, notFound = { throw UserNotFound() })
    }
}

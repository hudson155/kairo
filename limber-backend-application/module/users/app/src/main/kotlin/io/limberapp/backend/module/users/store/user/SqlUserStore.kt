package io.limberapp.backend.module.users.store.user

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.entity.user.AccountTable
import io.limberapp.backend.module.users.entity.user.UserTable
import io.limberapp.backend.module.users.exception.conflict.EmailAddressAlreadyTaken
import io.limberapp.backend.module.users.exception.conflict.UserAlreadyHasRole
import io.limberapp.backend.module.users.exception.conflict.UserDoesNotHaveRole
import io.limberapp.backend.module.users.exception.notFound.UserNotFound
import io.limberapp.backend.module.users.model.user.UserModel
import io.limberapp.backend.module.users.service.user.UserService
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import java.util.UUID

class SqlUserStore @Inject constructor(
    database: Database
) : UserService, SqlStore(database) {

    override fun create(model: UserModel) = transaction<Unit> {
        getByEmailAddress(model.emailAddress)?.let { throw EmailAddressAlreadyTaken(model.emailAddress) }
        AccountTable.insert { s ->
            s[createdDate] = model.created
            s[guid] = model.id
            s[name] = "${model.firstName} ${model.lastName}"
            s[identityProvider] = JwtRole.IDENTITY_PROVIDER in model.roles
            s[superuser] = JwtRole.SUPERUSER in model.roles
        }
        UserTable.insert { s ->
            s[createdDate] = model.created
            s[accountGuid] = model.id
            s[emailAddress] = model.emailAddress
            s[firstName] = model.firstName
            s[lastName] = model.lastName
            s[profilePhotoUrl] = model.profilePhotoUrl
        }
    }

    override fun get(userId: UUID) = transaction {
        return@transaction (UserTable innerJoin AccountTable)
            .select { AccountTable.guid eq userId }
            .singleOrNull()?.toUserModel()
    }

    override fun getByEmailAddress(emailAddress: String) = transaction {
        return@transaction (UserTable innerJoin AccountTable)
            .select { UserTable.emailAddress eq emailAddress }
            .singleOrNull()?.toUserModel()
    }

    override fun update(userId: UUID, update: UserModel.Update) = transaction {
        UserTable.update({ UserTable.accountGuid eq userId }) { s ->
            update.firstName?.let { s[firstName] = it }
            update.lastName?.let { s[lastName] = it }
        }
            .ifEq(0) { throw UserNotFound() }
            .ifGt(1, ::badSql)
        return@transaction checkNotNull(get(userId))
    }

    override fun addRole(userId: UUID, role: JwtRole) = transaction {
        val existing = get(userId) ?: throw UserNotFound()
        if (role in existing.roles) throw UserAlreadyHasRole(role)
        AccountTable.update({ AccountTable.guid eq userId }) { s ->
            when (role) {
                JwtRole.IDENTITY_PROVIDER -> s[identityProvider] = true
                JwtRole.SUPERUSER -> s[superuser] = true
            }
        }
            .ifGt(1, ::badSql)
        return@transaction checkNotNull(get(userId))
    }

    override fun removeRole(userId: UUID, role: JwtRole) = transaction {
        val existing = get(userId) ?: throw UserNotFound()
        if (role !in existing.roles) throw UserDoesNotHaveRole(role)
        AccountTable.update({ AccountTable.guid eq userId }) { s ->
            when (role) {
                JwtRole.IDENTITY_PROVIDER -> s[identityProvider] = false
                JwtRole.SUPERUSER -> s[superuser] = false
            }
        }
            .ifGt(1, ::badSql)
        return@transaction checkNotNull(get(userId))
    }

    override fun delete(userId: UUID) = transaction<Unit> {
        AccountTable.deleteWhere { AccountTable.guid eq userId }
            .ifEq(0) { throw UserNotFound() }
            .ifGt(1, ::badSql)
    }

    private fun ResultRow.toUserModel() = UserModel(
        id = this[AccountTable.guid],
        created = this[UserTable.createdDate],
        firstName = this[UserTable.firstName],
        lastName = this[UserTable.lastName],
        emailAddress = this[UserTable.emailAddress],
        profilePhotoUrl = this[UserTable.profilePhotoUrl],
        roles = mutableSetOf<JwtRole>().apply {
            if (this@toUserModel[AccountTable.identityProvider]) add(JwtRole.IDENTITY_PROVIDER)
            if (this@toUserModel[AccountTable.superuser]) add(JwtRole.SUPERUSER)
        }
    )
}

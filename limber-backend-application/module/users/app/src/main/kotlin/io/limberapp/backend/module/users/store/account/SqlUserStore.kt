package io.limberapp.backend.module.users.store.account

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.entity.account.AccountTable
import io.limberapp.backend.module.users.entity.account.UserTable
import io.limberapp.backend.module.users.exception.conflict.EmailAddressAlreadyTaken
import io.limberapp.backend.module.users.exception.conflict.UserAlreadyHasRole
import io.limberapp.backend.module.users.exception.conflict.UserDoesNotHaveRole
import io.limberapp.backend.module.users.exception.notFound.UserNotFound
import io.limberapp.backend.module.users.model.account.UserModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.util.UUID

class SqlUserStore @Inject constructor(
    database: Database
) : UserStore, SqlStore(database) {

    override fun create(model: UserModel) = transaction<Unit> {
        getByEmailAddress(model.emailAddress)?.let { throw EmailAddressAlreadyTaken(model.emailAddress) }
        AccountTable.insert { it.createAccount(model) }
        UserTable.insert { it.createUser(model) }
    }

    private fun InsertStatement<*>.createAccount(model: UserModel) {
        this[AccountTable.createdDate] = model.created
        this[AccountTable.guid] = model.id
        this[AccountTable.name] = "${model.firstName} ${model.lastName}"
        this[AccountTable.identityProvider] = JwtRole.IDENTITY_PROVIDER in model.roles
        this[AccountTable.superuser] = JwtRole.SUPERUSER in model.roles
    }

    private fun InsertStatement<*>.createUser(model: UserModel) {
        this[UserTable.createdDate] = model.created
        this[UserTable.accountGuid] = model.id
        this[UserTable.emailAddress] = model.emailAddress
        this[UserTable.firstName] = model.firstName
        this[UserTable.lastName] = model.lastName
        this[UserTable.profilePhotoUrl] = model.profilePhotoUrl
    }

    override fun get(userId: UUID) = transaction {
        return@transaction (UserTable innerJoin AccountTable)
            .select { AccountTable.guid eq userId }
            .singleOrNull()
            ?.toUserModel()
    }

    override fun getByEmailAddress(emailAddress: String) = transaction {
        return@transaction (UserTable innerJoin AccountTable)
            .select { UserTable.emailAddress eq emailAddress }
            .singleOrNull()
            ?.toUserModel()
    }

    override fun update(userId: UUID, update: UserModel.Update) = transaction {
        UserTable
            .updateAtMostOne(where = { UserTable.accountGuid eq userId }, body = { it.updateUser(update) })
            .ifEq(0) { throw UserNotFound() }
        return@transaction checkNotNull(get(userId))
    }

    override fun addRole(userId: UUID, role: JwtRole) = transaction {
        val existing = get(userId) ?: throw UserNotFound()
        if (role in existing.roles) throw UserAlreadyHasRole(role)
        AccountTable
            .updateAtMostOne(
                where = { AccountTable.guid eq userId },
                body = {
                    it.updateAccount(
                        identityProvider = if (role == JwtRole.IDENTITY_PROVIDER) true else null,
                        superuser = if (role == JwtRole.SUPERUSER) true else null
                    )
                })
        return@transaction checkNotNull(get(userId))
    }

    override fun removeRole(userId: UUID, role: JwtRole) = transaction {
        val existing = get(userId) ?: throw UserNotFound()
        if (role !in existing.roles) throw UserDoesNotHaveRole(role)
        AccountTable
            .updateAtMostOne(
                where = { AccountTable.guid eq userId },
                body = {
                    it.updateAccount(
                        identityProvider = if (role == JwtRole.IDENTITY_PROVIDER) false else null,
                        superuser = if (role == JwtRole.SUPERUSER) false else null
                    )
                })
        return@transaction checkNotNull(get(userId))
    }

    private fun UpdateStatement.updateAccount(identityProvider: Boolean? = null, superuser: Boolean? = null) {
        identityProvider?.let { this[AccountTable.identityProvider] = it }
        superuser?.let { this[AccountTable.superuser] = it }
    }

    private fun UpdateStatement.updateUser(update: UserModel.Update) {
        update.firstName?.let { this[UserTable.firstName] = it }
        update.lastName?.let { this[UserTable.lastName] = it }
    }

    override fun delete(userId: UUID) = transaction<Unit> {
        AccountTable
            .deleteAtMostOneWhere { AccountTable.guid eq userId }
            .ifEq(0) { throw UserNotFound() }
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

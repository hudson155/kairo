package io.limberapp.backend.module.users.store.account

import com.google.inject.Inject
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.entity.account.AccountTable
import io.limberapp.backend.module.users.entity.account.UserTable
import io.limberapp.backend.module.users.model.account.AccountModel
import io.limberapp.backend.module.users.model.account.UserModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement

internal class SqlAccountMapperImpl @Inject constructor() : SqlAccountMapper {

    override fun accountEntity(insertStatement: InsertStatement<*>, model: UserModel) {
        insertStatement[AccountTable.createdDate] = model.created
        insertStatement[AccountTable.guid] = model.id
        insertStatement[AccountTable.name] = "${model.firstName} ${model.lastName}"
        insertStatement[AccountTable.identityProvider] = JwtRole.IDENTITY_PROVIDER in model.roles
        insertStatement[AccountTable.superuser] = JwtRole.SUPERUSER in model.roles
    }

    override fun accountEntity(
        updateStatement: UpdateStatement,
        identityProvider: Boolean?,
        superuser: Boolean?
    ) {
        identityProvider?.let { updateStatement[AccountTable.identityProvider] = it }
        superuser?.let { updateStatement[AccountTable.superuser] = it }
    }

    override fun userEntity(insertStatement: InsertStatement<*>, model: UserModel) {
        insertStatement[UserTable.createdDate] = model.created
        insertStatement[UserTable.accountGuid] = model.id
        insertStatement[UserTable.orgGuid] = model.orgId
        insertStatement[UserTable.emailAddress] = model.emailAddress
        insertStatement[UserTable.firstName] = model.firstName
        insertStatement[UserTable.lastName] = model.lastName
        insertStatement[UserTable.profilePhotoUrl] = model.profilePhotoUrl
    }

    override fun userEntity(updateStatement: UpdateStatement, update: UserModel.Update) {
        update.firstName?.let { updateStatement[UserTable.firstName] = it }
        update.lastName?.let { updateStatement[UserTable.lastName] = it }
    }

    override fun accountModel(resultRow: ResultRow) = AccountModel(
        id = resultRow[AccountTable.guid],
        created = resultRow[AccountTable.createdDate],
        name = resultRow[AccountTable.name],
        roles = mutableSetOf<JwtRole>().apply {
            if (resultRow[AccountTable.identityProvider]) add(JwtRole.IDENTITY_PROVIDER)
            if (resultRow[AccountTable.superuser]) add(JwtRole.SUPERUSER)
        }
    )

    override fun userModel(resultRow: ResultRow) = UserModel(
        id = resultRow[AccountTable.guid],
        created = resultRow[UserTable.createdDate],
        orgId = resultRow[UserTable.orgGuid],
        firstName = resultRow[UserTable.firstName],
        lastName = resultRow[UserTable.lastName],
        emailAddress = resultRow[UserTable.emailAddress],
        profilePhotoUrl = resultRow[UserTable.profilePhotoUrl],
        roles = mutableSetOf<JwtRole>().apply {
            if (resultRow[AccountTable.identityProvider]) add(JwtRole.IDENTITY_PROVIDER)
            if (resultRow[AccountTable.superuser]) add(JwtRole.SUPERUSER)
        }
    )
}

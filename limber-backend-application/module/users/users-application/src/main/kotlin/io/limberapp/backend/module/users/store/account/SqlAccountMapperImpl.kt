package io.limberapp.backend.module.users.store.account

import com.google.inject.Inject
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
        insertStatement[AccountTable.identityProvider] = model.identityProvider
        insertStatement[AccountTable.superuser] = model.superuser
    }

    override fun userEntity(insertStatement: InsertStatement<*>, model: UserModel) {
        insertStatement[UserTable.createdDate] = model.created
        insertStatement[UserTable.guid] = model.id
        insertStatement[UserTable.name] = "${model.firstName} ${model.lastName}"
        insertStatement[UserTable.identityProvider] = model.identityProvider
        insertStatement[UserTable.superuser] = model.superuser
        insertStatement[UserTable.orgGuid] = model.orgId
        insertStatement[UserTable.emailAddress] = model.emailAddress
        insertStatement[UserTable.firstName] = model.firstName
        insertStatement[UserTable.lastName] = model.lastName
        insertStatement[UserTable.profilePhotoUrl] = model.profilePhotoUrl
    }

    override fun userEntity(updateStatement: UpdateStatement, update: UserModel.Update) {
        update.firstName?.let { updateStatement[UserTable.firstName] = it }
        update.lastName?.let { updateStatement[UserTable.lastName] = it }
        update.identityProvider?.let { updateStatement[UserTable.identityProvider] = it }
        update.superuser?.let { updateStatement[UserTable.superuser] = it }
    }

    override fun accountModel(resultRow: ResultRow) = AccountModel(
        id = resultRow[AccountTable.guid],
        created = resultRow[AccountTable.createdDate],
        name = resultRow[AccountTable.name],
        identityProvider = resultRow[AccountTable.identityProvider],
        superuser = resultRow[AccountTable.superuser]
    )

    override fun userModel(resultRow: ResultRow) = UserModel(
        id = resultRow[AccountTable.guid],
        created = resultRow[UserTable.createdDate],
        orgId = resultRow[UserTable.orgGuid],
        firstName = resultRow[UserTable.firstName],
        lastName = resultRow[UserTable.lastName],
        emailAddress = resultRow[UserTable.emailAddress],
        profilePhotoUrl = resultRow[UserTable.profilePhotoUrl],
        identityProvider = resultRow[AccountTable.identityProvider],
        superuser = resultRow[AccountTable.superuser]
    )
}

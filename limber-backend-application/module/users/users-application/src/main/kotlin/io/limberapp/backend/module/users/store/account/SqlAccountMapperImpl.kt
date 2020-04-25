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
        insertStatement[AccountTable.createdDate] = model.createdDate
        insertStatement[AccountTable.guid] = model.guid
        insertStatement[AccountTable.identityProvider] = model.identityProvider
        insertStatement[AccountTable.superuser] = model.superuser
        insertStatement[AccountTable.name] = "${model.firstName} ${model.lastName}"
    }

    override fun userEntity(insertStatement: InsertStatement<*>, model: UserModel) {
        insertStatement[UserTable.createdDate] = model.createdDate
        insertStatement[UserTable.guid] = model.guid
        insertStatement[UserTable.identityProvider] = model.identityProvider
        insertStatement[UserTable.superuser] = model.superuser
        insertStatement[UserTable.name] = "${model.firstName} ${model.lastName}"
        insertStatement[UserTable.orgGuid] = model.orgGuid
        insertStatement[UserTable.emailAddress] = model.emailAddress
        insertStatement[UserTable.firstName] = model.firstName
        insertStatement[UserTable.lastName] = model.lastName
        insertStatement[UserTable.profilePhotoUrl] = model.profilePhotoUrl
    }

    override fun userEntity(updateStatement: UpdateStatement, update: UserModel.Update) {
        update.identityProvider?.let { updateStatement[UserTable.identityProvider] = it }
        update.superuser?.let { updateStatement[UserTable.superuser] = it }
        update.firstName?.let { updateStatement[UserTable.firstName] = it }
        update.lastName?.let { updateStatement[UserTable.lastName] = it }
    }

    override fun accountModel(resultRow: ResultRow) = AccountModel(
        guid = resultRow[AccountTable.guid],
        createdDate = resultRow[AccountTable.createdDate],
        identityProvider = resultRow[AccountTable.identityProvider],
        superuser = resultRow[AccountTable.superuser],
        name = resultRow[AccountTable.name]
    )

    override fun userModel(resultRow: ResultRow) = UserModel(
        guid = resultRow[UserTable.guid],
        createdDate = resultRow[UserTable.createdDate],
        identityProvider = resultRow[UserTable.identityProvider],
        superuser = resultRow[UserTable.superuser],
        orgGuid = resultRow[UserTable.orgGuid],
        firstName = resultRow[UserTable.firstName],
        lastName = resultRow[UserTable.lastName],
        emailAddress = resultRow[UserTable.emailAddress],
        profilePhotoUrl = resultRow[UserTable.profilePhotoUrl]
    )
}

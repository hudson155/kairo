package io.limberapp.backend.module.users.store.account

import io.limberapp.backend.module.users.model.account.AccountModel
import io.limberapp.backend.module.users.model.account.UserModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement

interface SqlAccountMapper {

    fun accountEntity(insertStatement: InsertStatement<*>, model: UserModel)

    fun userEntity(insertStatement: InsertStatement<*>, model: UserModel)

    fun accountModel(resultRow: ResultRow): AccountModel

    fun userModel(resultRow: ResultRow): UserModel
}

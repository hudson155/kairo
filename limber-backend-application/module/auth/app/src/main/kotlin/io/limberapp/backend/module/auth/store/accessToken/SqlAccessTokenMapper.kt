package io.limberapp.backend.module.auth.store.accessToken

import io.limberapp.backend.module.auth.model.accessToken.AccessTokenModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement

interface SqlAccessTokenMapper {

    fun accessTokenEntity(insertStatement: InsertStatement<*>, model: AccessTokenModel)

    fun accessTokenModel(resultRow: ResultRow): AccessTokenModel
}

package io.limberapp.backend.module.auth.store.accessToken

import com.google.inject.Inject
import io.limberapp.backend.module.auth.entity.accessToken.AccessTokenTable
import io.limberapp.backend.module.auth.model.accessToken.AccessTokenModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement

internal class SqlAccessTokenMapperImpl @Inject constructor() : SqlAccessTokenMapper {

    override fun accessTokenEntity(insertStatement: InsertStatement<*>, model: AccessTokenModel) {
        insertStatement[AccessTokenTable.createdDate] = model.created
        insertStatement[AccessTokenTable.guid] = model.id
        insertStatement[AccessTokenTable.accountGuid] = model.userId
        insertStatement[AccessTokenTable.token] = model.token
    }

    override fun accessTokenModel(resultRow: ResultRow) = AccessTokenModel(
        id = resultRow[AccessTokenTable.guid],
        created = resultRow[AccessTokenTable.createdDate],
        userId = resultRow[AccessTokenTable.accountGuid],
        token = resultRow[AccessTokenTable.token]
    )
}

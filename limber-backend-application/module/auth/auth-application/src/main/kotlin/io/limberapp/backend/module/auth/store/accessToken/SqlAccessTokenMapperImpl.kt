package io.limberapp.backend.module.auth.store.accessToken

import com.google.inject.Inject
import io.limberapp.backend.module.auth.entity.accessToken.AccessTokenTable
import io.limberapp.backend.module.auth.model.accessToken.AccessTokenModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement

internal class SqlAccessTokenMapperImpl @Inject constructor() : SqlAccessTokenMapper {
    override fun accessTokenEntity(insertStatement: InsertStatement<*>, model: AccessTokenModel) {
        insertStatement[AccessTokenTable.createdDate] = model.createdDate
        insertStatement[AccessTokenTable.guid] = model.guid
        insertStatement[AccessTokenTable.accountGuid] = model.userGuid
        insertStatement[AccessTokenTable.encryptedSecret] = model.encryptedSecret
    }

    override fun accessTokenModel(resultRow: ResultRow) = AccessTokenModel(
        guid = resultRow[AccessTokenTable.guid],
        createdDate = resultRow[AccessTokenTable.createdDate],
        userGuid = resultRow[AccessTokenTable.accountGuid],
        encryptedSecret = resultRow[AccessTokenTable.encryptedSecret]
    )
}

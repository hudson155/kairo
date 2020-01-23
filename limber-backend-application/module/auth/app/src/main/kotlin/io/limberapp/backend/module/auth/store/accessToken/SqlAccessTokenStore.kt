package io.limberapp.backend.module.auth.store.accessToken

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.auth.entity.accessToken.AccessTokenTable
import io.limberapp.backend.module.auth.exception.accessToken.AccessTokenNotFound
import io.limberapp.backend.module.auth.model.accessToken.AccessTokenModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.util.UUID

internal class SqlAccessTokenStore @Inject constructor(
    database: Database
) : AccessTokenStore, SqlStore(database) {

    override fun create(model: AccessTokenModel) = transaction<Unit> {
        AccessTokenTable.insert { it.createAccessToken(model) }
    }

    private fun InsertStatement<*>.createAccessToken(model: AccessTokenModel) {
        this[AccessTokenTable.createdDate] = model.created
        this[AccessTokenTable.guid] = model.id
        this[AccessTokenTable.accountGuid] = model.userId
        this[AccessTokenTable.token] = model.token
    }

    override fun getByToken(token: String) = transaction {
        return@transaction AccessTokenTable
            .select { AccessTokenTable.token eq token }
            .singleOrNull()?.toAccessTokenModel()
    }

    override fun getByAccountId(userId: UUID) = transaction {
        return@transaction AccessTokenTable
            .select { AccessTokenTable.accountGuid eq userId }
            .map { it.toAccessTokenModel() }
    }

    override fun delete(userId: UUID, accessTokenId: UUID) = transaction<Unit> {
        AccessTokenTable
            .deleteAtMostOneWhere {
                (AccessTokenTable.accountGuid eq userId) and (AccessTokenTable.guid eq accessTokenId)
            }
            .ifEq(0) { throw AccessTokenNotFound() }
    }

    private fun ResultRow.toAccessTokenModel() = AccessTokenModel(
        id = this[AccessTokenTable.guid],
        created = this[AccessTokenTable.createdDate],
        userId = this[AccessTokenTable.accountGuid],
        token = this[AccessTokenTable.token]
    )
}

package io.limberapp.backend.module.auth.store.accessToken

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.auth.entity.accessToken.AccessTokenTable
import io.limberapp.backend.module.auth.exception.accessToken.AccessTokenNotFound
import io.limberapp.backend.module.auth.model.accessToken.AccessTokenModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.util.UUID

internal class SqlAccessTokenStore @Inject constructor(
    database: Database,
    private val sqlAccessTokenMapper: SqlAccessTokenMapper
) : AccessTokenStore, SqlStore(database) {

    override fun create(model: AccessTokenModel) = transaction<Unit> {
        AccessTokenTable.insert { sqlAccessTokenMapper.accesstokenEntity(it, model) }
    }

    override fun getByToken(token: String) = transaction {
        val entity = AccessTokenTable
            .select { AccessTokenTable.token eq token }
            .singleOrNull() ?: return@transaction null
        return@transaction sqlAccessTokenMapper.accessTokenModel(entity)
    }

    override fun getByAccountId(userId: UUID) = transaction {
        return@transaction AccessTokenTable
            .select { AccessTokenTable.accountGuid eq userId }
            .map { sqlAccessTokenMapper.accessTokenModel(it) }
            .toSet()
    }

    override fun delete(userId: UUID, accessTokenId: UUID) = transaction<Unit> {
        AccessTokenTable
            .deleteExactlyOne(
                where = { (AccessTokenTable.accountGuid eq userId) and (AccessTokenTable.guid eq accessTokenId) },
                notFound = { throw AccessTokenNotFound() }
            )
    }
}

package io.limberapp.backend.module.auth.store.accessToken

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.auth.entity.accessToken.AccessTokenTable
import io.limberapp.backend.module.auth.exception.accessToken.AccessTokenNotFound
import io.limberapp.backend.module.auth.model.accessToken.AccessTokenModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.mindrot.jbcrypt.BCrypt
import java.util.UUID

internal class SqlAccessTokenStore @Inject constructor(
    database: Database,
    private val sqlAccessTokenMapper: SqlAccessTokenMapper
) : AccessTokenStore, SqlStore(database) {
    override fun create(model: AccessTokenModel) = transaction<Unit> {
        AccessTokenTable.insert { sqlAccessTokenMapper.accessTokenEntity(it, model) }
    }

    override fun getIfValid(accessTokenGuid: UUID, accessTokenSecret: String) = transaction {
        val entity = AccessTokenTable
            .select { AccessTokenTable.guid eq accessTokenGuid }
            .singleNullOrThrow() ?: return@transaction null
        val model = sqlAccessTokenMapper.accessTokenModel(entity)
        if (!BCrypt.checkpw(accessTokenSecret, model.encryptedSecret)) return@transaction null
        return@transaction model
    }

    override fun getByAccountGuid(userGuid: UUID) = transaction {
        return@transaction AccessTokenTable
            .select { AccessTokenTable.accountGuid eq userGuid }
            .map { sqlAccessTokenMapper.accessTokenModel(it) }
            .toSet()
    }

    override fun delete(userGuid: UUID, accessTokenGuid: UUID) = transaction<Unit> {
        AccessTokenTable
            .deleteExactlyOne(
                where = { (AccessTokenTable.accountGuid eq userGuid) and (AccessTokenTable.guid eq accessTokenGuid) },
                notFound = { throw AccessTokenNotFound() }
            )
    }
}

package io.limberapp.backend.module.auth.store.personalAccessToken

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.auth.entity.personalAccessToken.PersonalAccessTokenTable
import io.limberapp.backend.module.auth.exception.notFound.PersonalAccessTokenNotFound
import io.limberapp.backend.module.auth.model.personalAccessToken.PersonalAccessTokenModel
import io.limberapp.backend.module.auth.service.personalAccessToken.PersonalAccessTokenService
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.util.UUID

internal class SqlPersonalAccessTokenStore @Inject constructor(
    database: Database
) : PersonalAccessTokenService, SqlStore(database) {

    override fun create(model: PersonalAccessTokenModel) = transaction<Unit> {
        PersonalAccessTokenTable.insert { s ->
            s[createdDate] = model.created
            s[guid] = model.id
            s[accountGuid] = model.userId
            s[token] = model.token
        }
    }

    override fun getByToken(token: String) = transaction {
        return@transaction PersonalAccessTokenTable.select { PersonalAccessTokenTable.token eq token }
            .singleOrNull()?.toPersonalAccessTokenModel()
    }

    override fun getByUserId(userId: UUID) = transaction {
        return@transaction PersonalAccessTokenTable.select { PersonalAccessTokenTable.accountGuid eq userId }
            .map { it.toPersonalAccessTokenModel() }
    }

    override fun delete(userId: UUID, personalAccessTokenId: UUID) = transaction<Unit> {
        PersonalAccessTokenTable.deleteWhere {
            (PersonalAccessTokenTable.accountGuid eq userId) and
                    (PersonalAccessTokenTable.guid eq personalAccessTokenId)
        }
            .ifEq(0) { throw PersonalAccessTokenNotFound() }
            .ifGt(1, ::badSql)
    }

    private fun ResultRow.toPersonalAccessTokenModel() = PersonalAccessTokenModel(
        id = this[PersonalAccessTokenTable.guid],
        created = this[PersonalAccessTokenTable.createdDate],
        userId = this[PersonalAccessTokenTable.accountGuid],
        token = this[PersonalAccessTokenTable.token]
    )
}

package io.limberapp.backend.module.auth.store.accessToken

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.auth.exception.accessToken.AccessTokenNotFound
import io.limberapp.backend.module.auth.model.accessToken.AccessTokenModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import java.util.UUID

internal class AccessTokenStore @Inject constructor(private val jdbi: Jdbi) : SqlStore() {
    fun create(model: AccessTokenModel) {
        jdbi.useHandle<Exception> {
            it.createUpdate(sqlResource("create")).bindKotlin(model).execute()
        }
    }

    fun get(accessTokenGuid: UUID): AccessTokenModel? {
        return jdbi.withHandle<AccessTokenModel?, Exception> {
            it.createQuery("SELECT * FROM auth.access_token WHERE guid = :guid")
                .bind("guid", accessTokenGuid)
                .mapTo(AccessTokenModel::class.java)
                .singleNullOrThrow() ?: return@withHandle null
        }
    }

    fun getByAccountGuid(accountGuid: UUID): Set<AccessTokenModel> {
        return jdbi.withHandle<Set<AccessTokenModel>, Exception> {
            it.createQuery("SELECT * FROM auth.access_token WHERE account_guid = :accountGuid")
                .bind("accountGuid", accountGuid)
                .mapTo(AccessTokenModel::class.java)
                .toSet()
        }
    }

    fun delete(accountGuid: UUID, accessTokenGuid: UUID) {
        jdbi.useTransaction<Exception> {
            val updateCount =
                it.createUpdate("DELETE FROM auth.access_token WHERE account_guid = :accountGuid AND guid = :guid")
                    .bind("accountGuid", accountGuid)
                    .bind("guid", accessTokenGuid)
                    .execute()
            when (updateCount) {
                0 -> throw AccessTokenNotFound()
                1 -> return@useTransaction
                else -> badSql()
            }
        }
    }
}

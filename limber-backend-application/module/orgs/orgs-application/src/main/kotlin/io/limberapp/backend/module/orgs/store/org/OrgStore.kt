package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.model.org.OrgModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import java.util.UUID

internal class OrgStore @Inject constructor(
    private val jdbi: Jdbi,
    private val featureStore: FeatureStore
) : SqlStore() {
    fun create(model: OrgModel) {
        jdbi.useTransaction<Exception> {
            it.createUpdate(sqlResource("create")).bindKotlin(model).execute()
            featureStore.create(model.guid, model.features)
        }
    }

    fun get(orgGuid: UUID): OrgModel? {
        return jdbi.withHandle<OrgModel?, Exception> {
            it.createQuery("SELECT * FROM orgs.org WHERE guid = :guid AND archived_date IS NULL")
                .bind("guid", orgGuid)
                .mapTo(OrgModel::class.java)
                .singleNullOrThrow()
                ?.copy(features = featureStore.getByOrgGuid(orgGuid))
        }
    }

    fun getByOwnerAccountGuid(ownerAccountGuid: UUID): Set<OrgModel> {
        val sql = "SELECT * FROM orgs.org WHERE owner_account_guid = :ownerAccountGuid AND archived_date IS NULL"
        return jdbi.withHandle<Set<OrgModel>, Exception> {
            it.createQuery(sql)
                .bind("ownerAccountGuid", ownerAccountGuid)
                .mapTo(OrgModel::class.java)
                .map { it.copy(features = featureStore.getByOrgGuid(it.guid)) }
                .toSet()
        }
    }

    fun update(orgGuid: UUID, update: OrgModel.Update): OrgModel {
        return jdbi.inTransaction<OrgModel, Exception> {
            val updateCount = it.createUpdate(sqlResource("update"))
                .bind("guid", orgGuid)
                .bindKotlin(update)
                .execute()
            when (updateCount) {
                0 -> throw OrgNotFound()
                1 -> return@inTransaction checkNotNull(get(orgGuid))
                else -> badSql()
            }
        }
    }

    fun delete(orgGuid: UUID) {
        val sql = "UPDATE orgs.org SET archived_date = NOW() WHERE guid = :guid AND archived_date IS NULL"
        jdbi.useTransaction<Exception> {
            val updateCount = it.createUpdate(sql)
                .bind("guid", orgGuid)
                .execute()
            when (updateCount) {
                0 -> throw OrgNotFound()
                1 -> return@useTransaction
                else -> badSql()
            }
        }
    }
}

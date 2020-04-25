package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.orgs.entity.org.OrgTable
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.model.org.OrgModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.util.UUID

internal class SqlOrgStore @Inject constructor(
    database: Database,
    private val featureStore: FeatureStore,
    private val sqlOrgMapper: SqlOrgMapper
) : OrgStore, SqlStore(database) {
    override fun create(model: OrgModel) = transaction {
        OrgTable.insert { sqlOrgMapper.orgEntity(it, model) }
        featureStore.create(model.guid, model.features)
    }

    override fun get(orgGuid: UUID) = transaction {
        val entity = OrgTable
            .select { OrgTable.guid eq orgGuid }
            .singleNullOrThrow() ?: return@transaction null
        return@transaction sqlOrgMapper.orgModel(entity)
    }

    override fun getByOwnerAccountGuid(ownerAccountGuid: UUID) = transaction {
        return@transaction OrgTable
            .select { OrgTable.ownerAccountGuid eq ownerAccountGuid }
            .map { sqlOrgMapper.orgModel(it) }
            .toSet()
    }

    override fun update(orgGuid: UUID, update: OrgModel.Update) = transaction {
        OrgTable
            .updateExactlyOne(
                where = { OrgTable.guid eq orgGuid },
                body = { sqlOrgMapper.orgEntity(it, update) },
                notFound = { throw OrgNotFound() }
            )
        return@transaction checkNotNull(get(orgGuid))
    }

    override fun delete(orgGuid: UUID) = transaction<Unit> {
        OrgTable.deleteExactlyOne(where = { OrgTable.guid eq orgGuid }, notFound = { throw OrgNotFound() })
    }
}

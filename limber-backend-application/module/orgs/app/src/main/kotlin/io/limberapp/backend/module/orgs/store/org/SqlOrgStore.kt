package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.orgs.entity.org.MembershipTable
import io.limberapp.backend.module.orgs.entity.org.OrgTable
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.model.org.OrgModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.util.UUID

internal class SqlOrgStore @Inject constructor(
    database: Database,
    private val featureStore: FeatureStore,
    private val membershipStore: MembershipStore,
    private val sqlOrgMapper: SqlOrgMapper
) : OrgStore, SqlStore(database) {

    override fun create(model: OrgModel) = transaction {
        OrgTable.insert { sqlOrgMapper.orgEntity(it, model) }
        featureStore.create(model.id, model.features)
        membershipStore.create(model.id, model.members)
    }

    override fun get(orgId: UUID) = transaction {
        val entity = OrgTable
            .select { OrgTable.guid eq orgId }
            .singleOrNull() ?: return@transaction null
        return@transaction sqlOrgMapper.orgModel(entity)
    }

    override fun getByMemberId(memberId: UUID) = transaction {
        return@transaction OrgTable
            .select {
                exists(
                    MembershipTable
                        .select {
                            (MembershipTable.orgGuid eq OrgTable.guid) and
                                    (MembershipTable.accountGuid eq memberId)
                        }
                )
            }
            .map { sqlOrgMapper.orgModel(it) }
    }

    override fun update(orgId: UUID, update: OrgModel.Update) = transaction {
        OrgTable
            .updateExactlyOne(
                where = { OrgTable.guid eq orgId },
                body = { it.updateOrg(update) },
                notFound = { throw OrgNotFound() }
            )
        return@transaction checkNotNull(get(orgId))
    }

    private fun UpdateStatement.updateOrg(update: OrgModel.Update) {
        update.name?.let { this[OrgTable.name] = it }
    }

    override fun delete(orgId: UUID) = transaction<Unit> {
        OrgTable.deleteExactlyOne(where = { OrgTable.guid eq orgId }, notFound = { throw OrgNotFound() })
    }
}

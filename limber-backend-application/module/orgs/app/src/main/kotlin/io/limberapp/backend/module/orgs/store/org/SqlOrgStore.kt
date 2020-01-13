package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.piperframework.module.annotation.Store
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.orgs.entity.org.MembershipTable
import io.limberapp.backend.module.orgs.entity.org.OrgTable
import io.limberapp.backend.module.orgs.exception.notFound.OrgNotFound
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.service.org.FeatureService
import io.limberapp.backend.module.orgs.service.org.MembershipService
import io.limberapp.backend.module.orgs.service.org.OrgService
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.update
import java.util.UUID

internal class SqlOrgStore @Inject constructor(
    database: Database,
    @Store private val featureStore: FeatureService,
    @Store private val membershipStore: MembershipService
) : OrgService, SqlStore(database) {

    override fun create(model: OrgModel) = transaction {
        OrgTable.insert { it.createOrg(model) }
        featureStore.create(model.id, model.features)
        membershipStore.create(model.id, model.members)
    }

    private fun InsertStatement<*>.createOrg(model: OrgModel) {
        this[OrgTable.createdDate] = model.created
        this[OrgTable.guid] = model.id
        this[OrgTable.name] = model.name
    }

    override fun get(orgId: UUID) = transaction {
        return@transaction OrgTable.select { OrgTable.guid eq orgId }
            .singleOrNull()?.toOrgModel()
    }

    override fun getByMemberId(memberId: UUID) = transaction {
        return@transaction OrgTable.select {
            exists(MembershipTable.select {
                (MembershipTable.orgGuid eq OrgTable.guid) and (MembershipTable.accountGuid eq memberId)
            })
        }
            .map { it.toOrgModel() }
    }

    override fun update(orgId: UUID, update: OrgModel.Update) = transaction {
        OrgTable.update({ OrgTable.guid eq orgId }) { s ->
            update.name?.let { s[name] = it }
        }
            .ifEq(0) { throw OrgNotFound() }
            .ifGt(1, ::badSql)
        return@transaction checkNotNull(get(orgId))
    }

    override fun delete(orgId: UUID) = transaction<Unit> {
        OrgTable.deleteAtMostOneWhere { OrgTable.guid eq orgId }
            .ifEq(0) { throw OrgNotFound() }
    }

    private fun ResultRow.toOrgModel(): OrgModel {
        val guid = this[OrgTable.guid]
        return OrgModel(
            id = guid,
            created = this[OrgTable.createdDate],
            name = this[OrgTable.name],
            features = featureStore.getByOrgId(guid),
            members = membershipStore.getByOrgId(guid)
        )
    }
}

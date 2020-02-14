package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isForeignKeyViolation
import com.piperframework.util.uuid.singleNullOrThrow
import io.limberapp.backend.module.orgs.entity.org.MembershipTable
import io.limberapp.backend.module.orgs.exception.org.MembershipNotFound
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.exception.org.UserIsAlreadyAMemberOfOrg
import io.limberapp.backend.module.orgs.model.org.MembershipModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.util.UUID

internal class SqlMembershipStore @Inject constructor(
    database: Database,
    private val sqlOrgMapper: SqlOrgMapper
) : MembershipStore, SqlStore(database) {

    override fun create(orgId: UUID, models: Set<MembershipModel>) = transaction {
        doOperation {
            MembershipTable.batchInsert(models) { model -> sqlOrgMapper.membershipEntity(this, orgId, model) }
        } andHandleError {
            when {
                error.isForeignKeyViolation(MembershipTable.orgGuidForeignKey) -> throw OrgNotFound()
            }
        }
    }

    override fun create(orgId: UUID, model: MembershipModel) = transaction {
        get(orgId, model.userId)?.let { throw UserIsAlreadyAMemberOfOrg() }
        doOperation { MembershipTable.insert { sqlOrgMapper.membershipEntity(it, orgId, model) } } andHandleError {
            when {
                error.isForeignKeyViolation(MembershipTable.orgGuidForeignKey) -> throw OrgNotFound()
            }
        }
    }

    override fun get(orgId: UUID, userId: UUID) = transaction {
        val entity = MembershipTable
            .select { (MembershipTable.orgGuid eq orgId) and (MembershipTable.accountGuid eq userId) }
            .singleNullOrThrow() ?: return@transaction null
        return@transaction sqlOrgMapper.membershipModel(entity)
    }

    override fun getByOrgId(orgId: UUID) = transaction {
        return@transaction MembershipTable
            .select { (MembershipTable.orgGuid eq orgId) }
            .map { sqlOrgMapper.membershipModel(it) }
            .toSet()
    }

    override fun delete(orgId: UUID, userId: UUID) = transaction<Unit> {
        MembershipTable
            .deleteExactlyOne(
                where = { (MembershipTable.orgGuid eq orgId) and (MembershipTable.accountGuid eq userId) },
                notFound = { throw MembershipNotFound() }
            )
    }
}

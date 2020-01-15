package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.orgs.entity.org.MembershipTable
import io.limberapp.backend.module.orgs.exception.conflict.UserIsAlreadyAMemberOfOrg
import io.limberapp.backend.module.orgs.exception.notFound.MembershipNotFound
import io.limberapp.backend.module.orgs.model.org.MembershipModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.util.UUID

internal class SqlMembershipStore @Inject constructor(
    database: Database
) : MembershipStore, SqlStore(database) {

    override fun create(orgId: UUID, models: List<MembershipModel>) = transaction<Unit> {
        MembershipTable.batchInsert(models) { model -> createMembership(orgId, model) }
    }

    override fun create(orgId: UUID, model: MembershipModel) = transaction<Unit> {

        get(orgId, model.userId)?.let { throw UserIsAlreadyAMemberOfOrg() }

        MembershipTable.insert { it.createMembership(orgId, model) }
    }

    private fun InsertStatement<*>.createMembership(orgId: UUID, model: MembershipModel) {
        this[MembershipTable.createdDate] = model.created
        this[MembershipTable.orgGuid] = orgId
        this[MembershipTable.accountGuid] = model.userId
    }

    override fun get(orgId: UUID, userId: UUID) = transaction {
        return@transaction MembershipTable.select {
            (MembershipTable.orgGuid eq orgId) and (MembershipTable.accountGuid eq userId)
        }.singleOrNull()?.toMembershipModel()
    }

    override fun getByOrgId(orgId: UUID) = transaction {
        return@transaction MembershipTable.select { (MembershipTable.orgGuid eq orgId) }
            .map { it.toMembershipModel() }
    }

    override fun delete(orgId: UUID, userId: UUID) = transaction<Unit> {
        MembershipTable.deleteAtMostOneWhere {
            (MembershipTable.orgGuid eq orgId) and (MembershipTable.accountGuid eq userId)
        }.ifEq(0) { throw MembershipNotFound() }
    }

    private fun ResultRow.toMembershipModel() = MembershipModel(
        created = this[MembershipTable.createdDate],
        userId = this[MembershipTable.accountGuid]
    )
}

package io.limberapp.backend.module.orgs.service.org

import io.limberapp.backend.module.orgs.entity.org.MembershipEntity
import io.limberapp.backend.module.orgs.entity.org.OrgEntity
import java.util.UUID

interface OrgService {

    fun create(entity: OrgEntity.Creation): OrgEntity.Complete

    fun get(id: UUID): OrgEntity.Complete?

    fun getByMemberId(memberId: UUID): List<OrgEntity.Complete>

    fun update(id: UUID, entity: OrgEntity.Update): OrgEntity.Complete

    fun createMembership(id: UUID, entity: MembershipEntity.Creation)

    fun deleteMembership(id: UUID, memberId: UUID)

    fun delete(id: UUID)
}

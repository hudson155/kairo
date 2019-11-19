package io.limberapp.backend.module.orgs.store.org

import io.limberapp.backend.module.orgs.entity.org.MembershipEntity
import io.limberapp.backend.module.orgs.entity.org.OrgEntity
import io.limberapp.framework.store.Store
import java.util.UUID

internal interface OrgStore : Store<OrgEntity, OrgEntity.Update> {

    fun getByMemberId(memberId: UUID): List<OrgEntity>

    fun createMembership(id: UUID, entity: MembershipEntity): Unit?

    fun deleteMembership(id: UUID, memberId: UUID): Unit?
}

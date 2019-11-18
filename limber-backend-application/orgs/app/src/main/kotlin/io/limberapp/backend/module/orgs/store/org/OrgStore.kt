package io.limberapp.backend.module.orgs.store.org

import io.limberapp.backend.module.orgs.entity.org.MembershipEntity
import io.limberapp.backend.module.orgs.entity.org.OrgEntity
import io.limberapp.framework.store.Store
import java.util.UUID

internal interface OrgStore :
    Store<OrgEntity.Complete, OrgEntity.Update> {

    fun getByMemberId(memberId: UUID): List<OrgEntity.Complete>

    fun createMembership(id: UUID, entity: MembershipEntity.Complete)

    fun deleteMembership(id: UUID, memberId: UUID)
}

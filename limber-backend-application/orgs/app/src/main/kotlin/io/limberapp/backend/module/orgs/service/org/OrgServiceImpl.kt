package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.entity.org.MembershipEntity
import io.limberapp.backend.module.orgs.entity.org.OrgEntity
import io.limberapp.backend.module.orgs.store.org.OrgStore
import io.limberapp.framework.store.create
import io.limberapp.framework.store.get
import io.limberapp.framework.store.update
import java.util.UUID

internal class OrgServiceImpl @Inject constructor(
    private val orgStore: OrgStore
) : OrgService {

    override fun create(entity: OrgEntity.Creation) = orgStore.create(entity)

    override fun get(id: UUID) = orgStore.get(id)

    override fun getByMemberId(memberId: UUID) = orgStore.getByMemberId(memberId)

    override fun update(id: UUID, entity: OrgEntity.Update) = orgStore.update(id, entity)

    override fun createMembership(id: UUID, entity: MembershipEntity.Creation) {
        orgStore.createMembership(id, entity)
    }

    override fun deleteMembership(id: UUID, memberId: UUID) {
        orgStore.deleteMembership(id, memberId)
    }

    override fun delete(id: UUID) = orgStore.delete(id)
}

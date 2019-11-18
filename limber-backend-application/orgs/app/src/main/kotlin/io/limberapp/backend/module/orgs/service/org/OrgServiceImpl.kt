package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.mapper.app.membership.MembershipMapper
import io.limberapp.backend.module.orgs.mapper.app.org.OrgMapper
import io.limberapp.backend.module.orgs.model.org.MembershipModel
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.store.org.OrgStore
import java.util.UUID

internal class OrgServiceImpl @Inject constructor(
    private val orgStore: OrgStore,
    private val membershipMapper: MembershipMapper,
    private val orgMapper: OrgMapper
) : OrgService {

    override fun create(model: OrgModel.Creation): OrgModel.Complete {
        val entity = orgMapper.entity(model)
        orgStore.create(entity)
        return orgMapper.completeModel(entity)
    }

    override fun get(id: UUID): OrgModel.Complete? {
        val entity = orgStore.get(id) ?: return null
        return orgMapper.completeModel(entity)
    }

    override fun getByMemberId(memberId: UUID): List<OrgModel.Complete> {
        val entities = orgStore.getByMemberId(memberId)
        return entities.map { orgMapper.completeModel(it) }
    }

    override fun update(id: UUID, model: OrgModel.Update): OrgModel.Complete {
        val update = orgMapper.update(model)
        val entity = orgStore.update(id, update)
        return orgMapper.completeModel(entity)
    }

    override fun createMembership(id: UUID, model: MembershipModel.Creation) {
        val entity = membershipMapper.entity(model)
        orgStore.createMembership(id, entity)
    }

    override fun deleteMembership(id: UUID, memberId: UUID) {
        orgStore.deleteMembership(id, memberId)
    }

    override fun delete(id: UUID) = orgStore.delete(id)
}

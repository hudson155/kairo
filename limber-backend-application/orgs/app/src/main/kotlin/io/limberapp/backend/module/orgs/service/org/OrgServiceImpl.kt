package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.mapper.app.membership.MembershipMapper
import io.limberapp.backend.module.orgs.mapper.app.org.OrgMapper
import io.limberapp.backend.module.orgs.model.org.MembershipModel
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.store.org.OrgStore
import io.limberapp.framework.store.create
import io.limberapp.framework.store.get
import io.limberapp.framework.store.update
import java.util.UUID

internal class OrgServiceImpl @Inject constructor(
    private val orgStore: OrgStore,
    private val membershipMapper: MembershipMapper,
    private val orgMapper: OrgMapper
) : OrgService {

    override fun create(model: OrgModel.Creation): OrgModel.Complete {
        val creationEntity = orgMapper.creationEntity(model)
        val completeEntity = orgStore.create(creationEntity)
        return orgMapper.completeModel(completeEntity)
    }

    override fun get(id: UUID): OrgModel.Complete? {
        val completeEntity = orgStore.get(id) ?: return null
        return orgMapper.completeModel(completeEntity)
    }

    override fun getByMemberId(memberId: UUID): List<OrgModel.Complete> {
        val completeEntities = orgStore.getByMemberId(memberId)
        return completeEntities.map { orgMapper.completeModel(it) }
    }

    override fun update(id: UUID, model: OrgModel.Update): OrgModel.Complete {
        val updateEntity = orgMapper.updateEntity(model)
        val completeEntity = orgStore.update(id, updateEntity)
        return orgMapper.completeModel(completeEntity)
    }

    override fun createMembership(id: UUID, model: MembershipModel.Creation) {
        val creationEntity = membershipMapper.creationEntity(model)
        orgStore.createMembership(id, creationEntity)
    }

    override fun deleteMembership(id: UUID, memberId: UUID) {
        orgStore.deleteMembership(id, memberId)
    }

    override fun delete(id: UUID) = orgStore.delete(id)
}

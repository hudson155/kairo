package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.mapper.app.membership.MembershipMapper
import io.limberapp.backend.module.orgs.mapper.app.org.OrgMapper
import io.limberapp.backend.module.orgs.model.org.MembershipModel
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.store.org.OrgStore
import io.limberapp.framework.exception.ConflictException
import io.limberapp.framework.exception.NotFoundException
import java.util.UUID

internal class OrgServiceImpl @Inject constructor(
    private val orgStore: OrgStore,
    private val membershipMapper: MembershipMapper,
    private val orgMapper: OrgMapper
) : OrgService {

    override fun create(model: OrgModel) {
        val entity = orgMapper.entity(model)
        orgStore.create(entity)
    }

    override fun get(id: UUID): OrgModel? {
        val entity = orgStore.get(id) ?: return null
        return orgMapper.model(entity)
    }

    override fun getByMemberId(memberId: UUID): List<OrgModel> {
        val entities = orgStore.getByMemberId(memberId)
        return entities.map { orgMapper.model(it) }
    }

    override fun update(id: UUID, update: OrgModel.Update): OrgModel {
        val entity = orgStore.update(id, orgMapper.update(update)) ?: throw NotFoundException()
        return orgMapper.model(entity)
    }

    override fun createMembership(id: UUID, model: MembershipModel) {
        get(id) ?: throw NotFoundException()
        val entity = membershipMapper.entity(model)
        orgStore.createMembership(id, entity) ?: throw ConflictException()
    }

    override fun deleteMembership(id: UUID, memberId: UUID) {
        orgStore.deleteMembership(id, memberId) ?: throw NotFoundException()
    }

    override fun delete(id: UUID) = orgStore.delete(id) ?: throw NotFoundException()
}

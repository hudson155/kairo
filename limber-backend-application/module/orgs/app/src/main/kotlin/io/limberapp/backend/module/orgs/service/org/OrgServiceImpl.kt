package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.mapper.app.org.OrgMapper
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.store.org.OrgStore
import java.util.UUID

internal class OrgServiceImpl @Inject constructor(
    private val orgStore: OrgStore,
    private val orgMapper: OrgMapper
) : OrgService {

    override fun create(model: OrgModel) {
        val entity = orgMapper.entity(model)
        orgStore.create(entity)
    }

    override fun get(orgId: UUID): OrgModel? {
        val entity = orgStore.get(orgId) ?: return null
        return orgMapper.model(entity)
    }

    override fun getByMemberId(memberId: UUID): List<OrgModel> {
        val entities = orgStore.getByMemberId(memberId)
        return entities.map { orgMapper.model(it) }
    }

    override fun update(orgId: UUID, update: OrgModel.Update): OrgModel {
        val entity = orgStore.update(orgId, orgMapper.update(update))
        return orgMapper.model(entity)
    }

    override fun delete(orgId: UUID) {
        orgStore.delete(orgId)
    }
}

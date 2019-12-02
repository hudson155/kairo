package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import com.piperframework.exception.ConflictException
import com.piperframework.exception.NotFoundException
import io.limberapp.backend.module.orgs.mapper.app.feature.FeatureMapper
import io.limberapp.backend.module.orgs.mapper.app.membership.MembershipMapper
import io.limberapp.backend.module.orgs.mapper.app.org.OrgMapper
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.model.org.MembershipModel
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.store.org.OrgStore
import java.util.UUID

internal class OrgServiceImpl @Inject constructor(
    private val orgStore: OrgStore,
    private val featureMapper: FeatureMapper,
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

    override fun createFeature(orgId: UUID, model: FeatureModel) {
        get(orgId) ?: throw NotFoundException()
        val entity = featureMapper.entity(model)
        orgStore.createFeature(orgId, entity) ?: throw ConflictException()
    }

    override fun updateFeature(orgId: UUID, featureId: UUID, update: FeatureModel.Update): FeatureModel {
        get(orgId)?.features?.singleOrNull { it.id == featureId } ?: throw NotFoundException()
        val entity = orgStore.updateFeature(orgId, featureId, featureMapper.update(update)) ?: throw ConflictException()
        return featureMapper.model(entity)
    }

    override fun deleteFeature(orgId: UUID, featureId: UUID) {
        orgStore.deleteFeature(orgId, featureId) ?: throw NotFoundException()
    }

    override fun createMembership(orgId: UUID, model: MembershipModel) {
        get(orgId) ?: throw NotFoundException()
        val entity = membershipMapper.entity(model)
        orgStore.createMembership(orgId, entity) ?: throw ConflictException()
    }

    override fun deleteMembership(orgId: UUID, memberId: UUID) {
        orgStore.deleteMembership(orgId, memberId) ?: throw NotFoundException()
    }

    override fun delete(id: UUID) = orgStore.delete(id) ?: throw NotFoundException()
}

package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import com.piperframework.exception.ConflictException
import com.piperframework.exception.NotFoundException
import io.limberapp.backend.module.orgs.mapper.app.feature.FeatureMapper
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.store.org.FeatureStore
import java.util.UUID

internal class FeatureServiceImpl @Inject constructor(
    private val orgService: OrgService,
    private val featureStore: FeatureStore,
    private val featureMapper: FeatureMapper
) : FeatureService {

    override fun create(orgId: UUID, model: FeatureModel) {
        orgService.get(orgId) ?: throw NotFoundException()
        val entity = featureMapper.entity(model)
        featureStore.create(orgId, entity) ?: throw ConflictException()
    }

    override fun update(orgId: UUID, id: UUID, update: FeatureModel.Update): FeatureModel {
        orgService.get(orgId)?.features?.singleOrNull { it.id == id } ?: throw NotFoundException()
        val entity = featureStore.update(orgId, id, featureMapper.update(update)) ?: throw ConflictException()
        return featureMapper.model(entity)
    }

    override fun delete(orgId: UUID, id: UUID) {
        featureStore.delete(orgId, id) ?: throw NotFoundException()
    }
}

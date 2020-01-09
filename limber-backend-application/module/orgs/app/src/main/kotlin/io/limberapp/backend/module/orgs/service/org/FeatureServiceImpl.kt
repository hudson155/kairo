package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.mapper.app.feature.FeatureMapper
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.store.org.FeatureStore
import java.util.UUID

internal class FeatureServiceImpl @Inject constructor(
    private val featureStore: FeatureStore,
    private val featureMapper: FeatureMapper
) : FeatureService {

    override fun create(orgId: UUID, model: FeatureModel) {
        val entity = featureMapper.entity(model)
        featureStore.create(orgId, entity)
    }

    override fun update(orgId: UUID, featureId: UUID, update: FeatureModel.Update): FeatureModel {
        val entity = featureStore.update(orgId, featureId, featureMapper.update(update))
        return featureMapper.model(entity)
    }

    override fun delete(orgId: UUID, featureId: UUID) {
        featureStore.delete(orgId, featureId)
    }
}

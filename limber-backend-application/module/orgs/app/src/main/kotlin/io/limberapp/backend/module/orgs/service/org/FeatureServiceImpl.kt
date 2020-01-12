package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import com.piperframework.module.annotation.Store
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import java.util.UUID

internal class FeatureServiceImpl @Inject constructor(
    @Store private val featureStore: FeatureService
) : FeatureService {

    override fun create(orgId: UUID, model: FeatureModel) = featureStore.create(orgId, model)

    override fun get(orgId: UUID, featureId: UUID) = featureStore.get(orgId, featureId)

    override fun update(orgId: UUID, featureId: UUID, update: FeatureModel.Update) =
        featureStore.update(orgId, featureId, update)

    override fun delete(orgId: UUID, featureId: UUID) = featureStore.delete(orgId, featureId)
}

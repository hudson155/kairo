package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.store.org.FeatureStore
import java.util.UUID

internal class FeatureServiceImpl @Inject constructor(
    private val featureStore: FeatureStore
) : FeatureService {

    override fun create(orgId: UUID, model: FeatureModel) = featureStore.create(orgId, model)

    override fun update(orgId: UUID, featureId: UUID, update: FeatureModel.Update) =
        featureStore.update(orgId, featureId, update)

    override fun delete(orgId: UUID, featureId: UUID) = featureStore.delete(orgId, featureId)
}

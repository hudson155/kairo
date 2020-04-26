package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.store.org.FeatureStore
import java.util.UUID

internal class FeatureServiceImpl @Inject constructor(
    private val featureStore: FeatureStore
) : FeatureService {
    override fun create(orgGuid: UUID, model: FeatureModel) = featureStore.create(orgGuid, model)

    override fun update(orgGuid: UUID, featureGuid: UUID, update: FeatureModel.Update) =
        featureStore.update(orgGuid, featureGuid, update)

    override fun delete(orgGuid: UUID, featureGuid: UUID) = featureStore.delete(orgGuid, featureGuid)
}

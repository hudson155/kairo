package io.limberapp.backend.module.orgs.service.feature

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.model.feature.FeatureModel
import io.limberapp.backend.module.orgs.store.feature.FeatureStore
import java.util.*

internal class FeatureServiceImpl @Inject constructor(
    private val featureStore: FeatureStore,
) : FeatureService {
  override fun create(model: FeatureModel) =
      featureStore.create(model)

  override fun get(featureGuid: UUID) =
      featureStore.get(featureGuid)

  override fun getByOrgGuid(orgGuid: UUID) =
      featureStore.getByOrgGuid(orgGuid)

  override fun update(orgGuid: UUID, featureGuid: UUID, update: FeatureModel.Update) =
      featureStore.update(orgGuid, featureGuid, update)

  override fun delete(orgGuid: UUID, featureGuid: UUID) =
      featureStore.delete(orgGuid, featureGuid)
}

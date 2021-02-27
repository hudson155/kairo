package io.limberapp.service.feature

import com.google.inject.Inject
import io.limberapp.model.feature.FeatureModel
import io.limberapp.store.feature.FeatureStore
import java.util.UUID

internal class FeatureServiceImpl @Inject constructor(
    private val featureStore: FeatureStore,
) : FeatureService {
  override fun create(model: FeatureModel): FeatureModel =
      featureStore.create(model)

  override fun get(featureGuid: UUID): FeatureModel? =
      featureStore[featureGuid]

  override fun getByOrgGuid(orgGuid: UUID): List<FeatureModel> =
      featureStore.getByOrgGuid(orgGuid)

  override fun update(featureGuid: UUID, update: FeatureModel.Update): FeatureModel =
      featureStore.update(featureGuid, update)

  override fun delete(featureGuid: UUID): Unit =
      featureStore.delete(featureGuid)
}

package io.limberapp.backend.module.orgs.service.feature

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.model.org.FeatureFinder
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.service.org.FeatureService
import io.limberapp.backend.module.orgs.store.feature.FeatureStore
import io.limberapp.common.finder.Finder
import java.util.*

internal class FeatureServiceImpl @Inject constructor(
  private val featureStore: FeatureStore,
) : FeatureService, Finder<FeatureModel, FeatureFinder> by featureStore {
  override fun create(model: FeatureModel) =
    featureStore.create(model)

  override fun update(orgGuid: UUID, featureGuid: UUID, update: FeatureModel.Update) =
    featureStore.update(orgGuid, featureGuid, update)

  override fun delete(orgGuid: UUID, featureGuid: UUID) =
    featureStore.delete(orgGuid, featureGuid)
}

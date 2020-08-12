package io.limberapp.backend.module.orgs.service.feature

import com.google.inject.Inject
import com.piperframework.finder.Finder
import com.piperframework.util.uuid.UuidGenerator
import io.limberapp.backend.module.orgs.model.org.FeatureFinder
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.service.org.FeatureService
import io.limberapp.backend.module.orgs.store.feature.FeatureStore
import java.time.Clock
import java.time.LocalDateTime
import java.util.*

internal class FeatureServiceImpl @Inject constructor(
  private val clock: Clock,
  private val uuidGenerator: UuidGenerator,
  private val featureStore: FeatureStore
) : FeatureService, Finder<FeatureModel, FeatureFinder> by featureStore {
  override fun createDefaults(orgGuid: UUID): List<FeatureModel> {
    require(featureStore.findAsSet { orgGuid(orgGuid) }.isEmpty())
    val feature = FeatureModel(
      guid = uuidGenerator.generate(),
      createdDate = LocalDateTime.now(clock),
      orgGuid = orgGuid,
      rank = 0,
      name = "Home",
      path = "/home",
      type = FeatureModel.Type.HOME,
      isDefaultFeature = true
    )
    featureStore.create(feature)
    return listOf(feature)
  }

  override fun create(model: FeatureModel) =
    featureStore.create(model)

  override fun update(orgGuid: UUID, featureGuid: UUID, update: FeatureModel.Update) =
    featureStore.update(orgGuid, featureGuid, update)

  override fun delete(orgGuid: UUID, featureGuid: UUID) =
    featureStore.delete(orgGuid, featureGuid)
}

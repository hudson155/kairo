package io.limberapp.backend.module.orgs.service.feature

import io.limberapp.backend.LimberModule
import io.limberapp.backend.module.orgs.model.feature.FeatureFinder
import io.limberapp.backend.module.orgs.model.feature.FeatureModel
import io.limberapp.common.finder.Finder
import java.util.*

@LimberModule.Orgs
interface FeatureService : Finder<FeatureModel, FeatureFinder> {
  fun create(model: FeatureModel): FeatureModel

  fun update(orgGuid: UUID, featureGuid: UUID, update: FeatureModel.Update): FeatureModel

  fun delete(orgGuid: UUID, featureGuid: UUID)
}

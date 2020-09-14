package io.limberapp.backend.module.orgs.service.org

import io.limberapp.backend.LimberModule
import io.limberapp.backend.module.orgs.model.org.FeatureFinder
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.common.finder.Finder
import java.util.*

@LimberModule.Orgs
interface FeatureService : Finder<FeatureModel, FeatureFinder> {
  fun create(model: FeatureModel): FeatureModel

  fun update(orgGuid: UUID, featureGuid: UUID, update: FeatureModel.Update): FeatureModel

  fun delete(orgGuid: UUID, featureGuid: UUID)
}

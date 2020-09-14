package io.limberapp.backend.module.orgs.service.org

import com.piperframework.finder.Finder
import io.limberapp.backend.LimberModule
import io.limberapp.backend.module.orgs.model.org.FeatureFinder
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import java.util.*

@LimberModule.Orgs
interface FeatureService : Finder<FeatureModel, FeatureFinder> {
  fun create(model: FeatureModel): FeatureModel

  fun update(orgGuid: UUID, featureGuid: UUID, update: FeatureModel.Update): FeatureModel

  fun delete(orgGuid: UUID, featureGuid: UUID)
}

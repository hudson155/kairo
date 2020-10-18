package io.limberapp.backend.module.orgs.service.feature

import io.limberapp.backend.module.orgs.model.feature.FeatureModel
import java.util.*

interface FeatureService {
  fun create(model: FeatureModel): FeatureModel

  fun get(featureGuid: UUID): FeatureModel?

  fun getByOrgGuid(orgGuid: UUID): List<FeatureModel>

  fun update(orgGuid: UUID, featureGuid: UUID, update: FeatureModel.Update): FeatureModel

  fun delete(orgGuid: UUID, featureGuid: UUID)
}

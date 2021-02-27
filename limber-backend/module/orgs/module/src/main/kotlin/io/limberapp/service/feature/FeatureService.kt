package io.limberapp.service.feature

import io.limberapp.model.feature.FeatureModel
import java.util.UUID

interface FeatureService {
  fun create(model: FeatureModel): FeatureModel

  operator fun get(featureGuid: UUID): FeatureModel?

  fun getByOrgGuid(orgGuid: UUID): List<FeatureModel>

  fun update(featureGuid: UUID, update: FeatureModel.Update): FeatureModel

  fun delete(featureGuid: UUID)
}

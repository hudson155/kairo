package io.limberapp.backend.module.orgs.service.org

import io.limberapp.backend.module.orgs.model.org.FeatureModel
import java.util.*

interface FeatureService {
  fun createDefaults(orgGuid: UUID): List<FeatureModel>

  fun create(model: FeatureModel): FeatureModel

  fun getByOrgGuid(orgGuid: UUID): List<FeatureModel>

  fun update(orgGuid: UUID, featureGuid: UUID, update: FeatureModel.Update): FeatureModel

  fun delete(orgGuid: UUID, featureGuid: UUID)
}

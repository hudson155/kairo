package io.limberapp.backend.module.auth.service.feature

import io.limberapp.backend.LimberModule
import io.limberapp.backend.module.auth.model.feature.FeatureRoleModel
import java.util.*

@LimberModule.Auth
interface FeatureRoleService {
  fun create(model: FeatureRoleModel): FeatureRoleModel

  fun getByFeatureGuid(featureGuid: UUID): Set<FeatureRoleModel>

  fun update(featureGuid: UUID, featureRoleGuid: UUID, update: FeatureRoleModel.Update): FeatureRoleModel

  fun delete(featureGuid: UUID, featureRoleGuid: UUID)
}

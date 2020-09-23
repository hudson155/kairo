package io.limberapp.backend.module.auth.service.feature

import io.limberapp.backend.LimberModule
import io.limberapp.backend.module.auth.model.feature.FeatureRoleFinder
import io.limberapp.backend.module.auth.model.feature.FeatureRoleModel
import io.limberapp.common.finder.Finder
import java.util.*

@LimberModule.Auth
interface FeatureRoleService : Finder<FeatureRoleModel, FeatureRoleFinder> {
  fun create(model: FeatureRoleModel): FeatureRoleModel

  fun update(featureGuid: UUID, featureRoleGuid: UUID, update: FeatureRoleModel.Update): FeatureRoleModel

  fun delete(featureGuid: UUID, featureRoleGuid: UUID)
}

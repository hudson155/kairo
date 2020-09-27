package io.limberapp.backend.module.auth.model.feature

import io.limberapp.backend.LimberModule
import java.util.*

@LimberModule.Auth
interface FeatureRoleFinder {
  fun featureGuid(featureGuid: UUID)
  fun featureRoleGuid(featureRoleGuid: UUID)
  fun orgRoleGuids(orgRoleGuids: Set<UUID>)
}

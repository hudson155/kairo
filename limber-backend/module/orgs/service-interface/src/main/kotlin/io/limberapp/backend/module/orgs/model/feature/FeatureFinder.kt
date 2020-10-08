package io.limberapp.backend.module.orgs.model.feature

import io.limberapp.backend.LimberModule
import java.util.*

@LimberModule.Orgs
interface FeatureFinder {
  fun orgGuid(orgGuid: UUID)
  fun featureGuid(featureGuid: UUID)
}

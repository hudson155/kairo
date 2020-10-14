package io.limberapp.backend.module.orgs.model.feature

import java.util.*

interface FeatureFinder {
  fun orgGuid(orgGuid: UUID)
  fun featureGuid(featureGuid: UUID)
}

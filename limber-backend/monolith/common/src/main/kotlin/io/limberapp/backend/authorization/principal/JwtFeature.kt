package io.limberapp.backend.authorization.principal

import io.limberapp.backend.authorization.permissions.featurePermissions.FeaturePermissions

data class JwtFeature(
  val permissions: FeaturePermissions,
)

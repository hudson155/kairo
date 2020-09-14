package io.limberapp.backend.authorization.principal

import io.limberapp.backend.authorization.permissions.featurePermissions.FeaturePermissions
import kotlinx.serialization.Serializable

@Serializable
data class JwtFeature(
  val permissions: FeaturePermissions,
)

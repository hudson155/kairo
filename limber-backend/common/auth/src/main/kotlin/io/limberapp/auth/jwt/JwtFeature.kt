package io.limberapp.auth.jwt

import io.limberapp.permissions.featurePermissions.FeaturePermissions

data class JwtFeature(
    val permissions: FeaturePermissions,
)

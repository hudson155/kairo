package io.limberapp.auth.jwt

import io.limberapp.permissions.feature.FeaturePermissions

data class JwtFeature(
    val permissions: FeaturePermissions,
)

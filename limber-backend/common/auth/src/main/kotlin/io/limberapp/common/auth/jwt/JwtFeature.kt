package io.limberapp.common.auth.jwt

import io.limberapp.permissions.featurePermissions.FeaturePermissions

data class JwtFeature(
    val permissions: FeaturePermissions,
)

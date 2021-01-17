package io.limberapp.common.auth.jwt

import io.limberapp.common.permissions.featurePermissions.FeaturePermissions

data class JwtFeature(
    val permissions: FeaturePermissions,
)

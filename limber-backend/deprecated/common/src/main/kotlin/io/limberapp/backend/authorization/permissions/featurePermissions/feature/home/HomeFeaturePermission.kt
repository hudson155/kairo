package io.limberapp.backend.authorization.permissions.featurePermissions.feature.home

import io.limberapp.backend.authorization.permissions.featurePermissions.FeaturePermission

@Suppress("MagicNumber")
enum class HomeFeaturePermission(
    override val bit: Int,
    override val title: String,
    override val description: String,
) : FeaturePermission

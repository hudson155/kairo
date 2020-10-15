package io.limberapp.permissions.featurePermissions.feature.home

import io.limberapp.permissions.featurePermissions.FeaturePermission

@Suppress("MagicNumber")
enum class HomeFeaturePermission(
    override val bit: Int,
    override val title: String,
    override val description: String,
) : FeaturePermission

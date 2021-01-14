package io.limberapp.common.permissions.featurePermissions

class TestFeaturePermissionsDeserializer : FeaturePermissionsDeserializer(mapOf(
    'T' to { TestFeaturePermissions.fromDarb(it) },
))

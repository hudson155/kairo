package io.limberapp.common.auth.jwt

import io.limberapp.common.permissions.featurePermissions.FeaturePermissionsDeserializer

class TestFeaturePermissionsDeserializer : FeaturePermissionsDeserializer(mapOf(
    'A' to { TestFeaturePermissionsA.fromDarb(it) },
    'B' to { TestFeaturePermissionsB.fromDarb(it) },
))

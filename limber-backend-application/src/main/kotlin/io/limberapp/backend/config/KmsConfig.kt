package io.limberapp.backend.config

internal data class KmsConfig(
    val project: String,
    val location: String,
    val keyRing: String,
    val cryptoKey: String
)

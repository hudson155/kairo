package io.limberapp.framework.config.jwt

/**
 * This class encapsulates the configuration for JWT authentication.
 */
data class JwtConfig(
    val domain: String? = null,
    val requireSignature: Boolean = true
)

package io.limberapp.error

/**
 * This class is the only JSON error that is returned as an HTTP response body.
 */
data class LimberError(
    val statusCode: Int,
    val statusCodeDescription: String,
    val message: String?,
)

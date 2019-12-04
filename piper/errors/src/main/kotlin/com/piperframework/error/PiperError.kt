package com.piperframework.error

/**
 * This class is the only JSON error that is returned.
 */
data class PiperError(
    val statusCode: Int,
    val message: String
)

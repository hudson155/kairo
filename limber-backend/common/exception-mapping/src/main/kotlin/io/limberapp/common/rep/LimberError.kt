package io.limberapp.common.rep

data class LimberError(
    val statusCode: Int,
    val statusCodeDescription: String,
    val message: String?,
)

package io.limberapp.common.error

/**
 * This class is the only JSON error that is returned.
 */
data class LimberError(
  val statusCode: Int,
  val statusCodeDescription: String,
  val message: String?,
)

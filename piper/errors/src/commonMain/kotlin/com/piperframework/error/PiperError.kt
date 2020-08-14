package com.piperframework.error

import kotlinx.serialization.Serializable

/**
 * This class is the only JSON error that is returned.
 */
@Serializable
data class PiperError(
  val statusCode: Int,
  val statusCodeDescription: String,
  val message: String?,
)

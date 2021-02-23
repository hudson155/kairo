package io.limberapp.exception

import io.ktor.http.HttpStatusCode

abstract class LimberException internal constructor(
    internal val limberMessage: String, // Name chosen to avoid conflict with built-in.
    internal val userVisibleProperties: Map<String, Any>,
    internal val limberCause: Exception?, // Name chosen to avoid conflict with built-in.
) : Exception(limberMessage, limberCause) {
  abstract val statusCode: HttpStatusCode

  val properties: Map<String, Any> by lazy {
    userVisibleProperties.plus(listOf(
        "error" to checkNotNull(this::class.simpleName),
        "statusCode" to statusCode.value,
        "statusCodeDescription" to statusCode.description,
        "message" to limberMessage,
    ))
  }
}

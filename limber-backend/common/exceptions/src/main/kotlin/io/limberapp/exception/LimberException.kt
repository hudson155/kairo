package io.limberapp.exception

abstract class LimberException(
  message: String,
  val developerMessage: String?,
  cause: Exception? = null,
) : Exception(message, cause)

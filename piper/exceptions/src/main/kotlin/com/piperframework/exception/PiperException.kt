package com.piperframework.exception

abstract class PiperException(
  message: String,
  val developerMessage: String?,
  cause: Exception? = null
) : Exception(message, cause)

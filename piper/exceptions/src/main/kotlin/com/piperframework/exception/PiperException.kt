package com.piperframework.exception

abstract class PiperException(
    message: String,
    val developerMessage: String?,
    cause: Throwable? = null
) : Exception(message, cause)

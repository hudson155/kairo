package com.piperframework.exception

abstract class PiperException(
    message: String,
    developerMessage: String?,
    cause: Throwable? = null
) : Exception(message, cause)

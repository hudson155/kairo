package com.piperframework.exception

abstract class PiperException(message: String, cause: Throwable? = null) : Exception(message, cause)

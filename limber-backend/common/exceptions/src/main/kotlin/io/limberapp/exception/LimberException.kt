package io.limberapp.exception

abstract class LimberException(
    override val message: String,
    val developerMessage: String?,
    override val cause: Exception? = null,
) : Exception(message, cause)

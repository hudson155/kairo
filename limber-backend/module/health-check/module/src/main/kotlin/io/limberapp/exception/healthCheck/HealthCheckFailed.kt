package io.limberapp.exception.healthCheck

import io.limberapp.exception.InternalServerErrorException

internal class HealthCheckFailed(message: String, cause: Exception?) :
    InternalServerErrorException(message, cause)

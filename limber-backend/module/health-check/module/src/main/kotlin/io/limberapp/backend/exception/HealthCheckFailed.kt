package io.limberapp.backend.exception

import io.limberapp.common.exception.internalServerError.InternalServerErrorException

internal class HealthCheckFailed(message: String, cause: Exception?) :
    InternalServerErrorException(message, cause)

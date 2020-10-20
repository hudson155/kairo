package io.limberapp.common.endpoint.exception

import io.limberapp.common.exception.badRequest.BadRequestException

class ParameterConversionException(
    message: String = "Could not convert parameter to the appropriate type.",
    cause: Exception? = null,
) : BadRequestException(message, cause)

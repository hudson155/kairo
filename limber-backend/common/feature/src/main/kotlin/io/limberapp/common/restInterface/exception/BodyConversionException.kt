package io.limberapp.common.restInterface.exception

import io.limberapp.common.exception.badRequest.BadRequestException

class BodyConversionException(cause: Exception) : BadRequestException(
    message = "Could not convert body to the appropriate type.",
    cause = cause,
)

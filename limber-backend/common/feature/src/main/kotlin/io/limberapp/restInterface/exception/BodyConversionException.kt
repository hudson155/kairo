package io.limberapp.restInterface.exception

import io.limberapp.exception.BadRequestException

class BodyConversionException(cause: Exception) : BadRequestException(
    message = "Could not convert body to the appropriate type.",
    cause = cause,
)

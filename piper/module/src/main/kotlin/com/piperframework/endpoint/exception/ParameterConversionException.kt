package com.piperframework.endpoint.exception

import com.piperframework.exception.exception.badRequest.BadRequestException

class ParameterConversionException(
    message: String = "Could not convert parameter to the appropriate type.",
    cause: Exception? = null
) : BadRequestException(message, cause)

package io.limberapp.restInterface.exception

import io.limberapp.exception.BadRequestException

class MissingParameterException(parameterName: String) : BadRequestException(
    message = "Missing parameter.",
    userVisibleProperties = mapOf("parameterName" to parameterName),
)

package io.limberapp.common.restInterface.exception

import io.limberapp.common.exception.badRequest.BadRequestException

class MissingParameterException(parameterName: String) : BadRequestException(
    message = "Missing parameter.",
    userVisibleProperties = mapOf("parameterName" to parameterName),
)

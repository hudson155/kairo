package io.limberapp.common.restInterface.exception

import io.limberapp.common.exception.badRequest.BadRequestException

class ValidationException(propertyNames: List<String>) : BadRequestException(
    message = "Invalid properties.",
    userVisibleProperties = mapOf("propertyNames" to propertyNames),
)

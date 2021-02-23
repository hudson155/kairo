package io.limberapp.restInterface.exception

import io.limberapp.exception.BadRequestException

class ValidationException(propertyNames: List<String>) : BadRequestException(
    message = "Invalid properties.",
    userVisibleProperties = mapOf("propertyNames" to propertyNames),
)

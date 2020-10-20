package io.limberapp.common.endpoint.exception

import io.limberapp.common.exception.badRequest.BadRequestException

class ValidationException(
    val propertyName: String,
) : BadRequestException("Invalid $propertyName.")

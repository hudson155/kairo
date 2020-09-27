package io.limberapp.common.endpoint.exception

import io.limberapp.exception.badRequest.BadRequestException

class ValidationException(
  val propertyName: String,
) : BadRequestException("Invalid $propertyName.")

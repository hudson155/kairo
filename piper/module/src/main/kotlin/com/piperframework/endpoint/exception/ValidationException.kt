package com.piperframework.endpoint.exception

import com.piperframework.exception.exception.badRequest.BadRequestException

class ValidationException constructor(
  val propertyName: String
) : BadRequestException("Invalid $propertyName.")

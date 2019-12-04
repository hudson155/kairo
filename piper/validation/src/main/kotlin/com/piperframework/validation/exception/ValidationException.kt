package com.piperframework.validation.exception

import com.piperframework.exception.exception.badRequest.BadRequestException

internal class ValidationException(val propertyName: String) : BadRequestException("Invalid $propertyName.")


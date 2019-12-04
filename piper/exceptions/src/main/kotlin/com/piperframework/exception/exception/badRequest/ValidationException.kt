package com.piperframework.exception.exception.badRequest

class ValidationException(val propertyName: String) : BadRequestException("Invalid $propertyName.")

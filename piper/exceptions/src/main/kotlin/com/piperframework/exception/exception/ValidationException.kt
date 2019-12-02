package com.piperframework.exception.exception

import com.piperframework.exception.PiperException

class ValidationException(val propertyName: String) : PiperException()

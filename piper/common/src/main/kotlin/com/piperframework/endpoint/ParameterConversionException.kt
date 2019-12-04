package com.piperframework.endpoint

import com.piperframework.exception.exception.badRequest.BadRequestException
import kotlin.reflect.KClass

class ParameterConversionException(name: String?, clazz: KClass<*>, cause: Throwable? = null) :
    BadRequestException(
        message = "Could not convert parameter${name?.let { " $it" } ?: ""} to ${clazz.simpleName ?: "?"}.",
        cause = cause
    )

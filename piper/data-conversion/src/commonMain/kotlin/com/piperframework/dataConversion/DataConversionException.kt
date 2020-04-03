package com.piperframework.dataConversion

import kotlin.reflect.KClass

class DataConversionException(name: String?, klass: KClass<*>, cause: Exception? = null) :
    Exception("Could not convert parameter${name?.let { " $it" } ?: ""} to ${klass.simpleName ?: "?"}.", cause)

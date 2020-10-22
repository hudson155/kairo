package io.limberapp.common.typeConversion.exception

import kotlin.reflect.KClass

class TypeConversionException(name: String?, kClass: KClass<*>, cause: Exception? = null) :
    Exception("Could not convert parameter${name?.let { " $it" } ?: ""} to ${kClass.simpleName ?: "?"}.", cause)

package io.limberapp.common.dataConversion

import kotlin.reflect.KClass

class DataConversionException(name: String?, kClass: KClass<*>, cause: Exception? = null) :
  Exception("Could not convert parameter${name?.let { " $it" } ?: ""} to ${kClass.simpleName ?: "?"}.", cause)

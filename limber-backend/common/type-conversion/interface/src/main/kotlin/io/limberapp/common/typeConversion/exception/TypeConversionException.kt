package io.limberapp.common.typeConversion.exception

import kotlin.reflect.KClass

class TypeConversionException(
    kClass: KClass<*>,
    additionalMessage: String,
) : Exception("Could not convert parameter to ${kClass.simpleName ?: "?"}. $additionalMessage")

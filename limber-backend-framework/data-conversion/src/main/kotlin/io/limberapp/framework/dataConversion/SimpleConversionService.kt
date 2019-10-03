package io.limberapp.framework.dataConversion

import io.ktor.util.ConversionService
import io.ktor.util.DataConversionException
import java.lang.reflect.Type
import kotlin.reflect.KClass

/**
 * The SimpleConversionService simplifies the ConversionService interface to 3 basic methods.
 *
 * isValid(), which should return true iff the string is valid for conversion to the type.
 * fromValue(), which converts from a validated string to an instance of the type.
 * toValue(), which converts from an instance of the type to a string.
 */
abstract class SimpleConversionService<T : Any>(val clazz: KClass<T>) : ConversionService {

    final override fun fromValues(values: List<String>, type: Type): Any? {
        return when (values.size) {
            0 -> null
            1 -> {
                val value = values.single()
                if (!isValid(value)) {
                    throw DataConversionException("Cannot convert - value is malformed")
                }
                fromValue(value)
            }
            else -> throw DataConversionException("Cannot convert - multiple values specified")
        }
    }

    abstract fun isValid(value: String): Boolean

    abstract fun fromValue(value: String): T

    final override fun toValues(value: Any?): List<String> {
        value ?: return emptyList()
        if (value::class != clazz) {
            throw DataConversionException("Cannot convert - value is not a ${clazz.simpleName}")
        }
        // Not sure why the function signature is Any?. If this cast ever causes an issue, remove
        // the suppression and fix the issue, but for now we'll leave it.
        @Suppress("UNCHECKED_CAST", "UnsafeCast")
        return listOf(toValue(value as T))
    }

    abstract fun toValue(value: T): String
}

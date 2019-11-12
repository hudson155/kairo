package io.limberapp.framework

import io.ktor.util.ConversionService
import io.ktor.util.DataConversionException
import io.limberapp.framework.dataConversion.DataConversionService
import java.lang.reflect.Type

fun <T : Any> conversionService(
    dataConversionService: DataConversionService<T>
) = object : ConversionService {
    override fun fromValues(values: List<String>, type: Type): Any? {
        return when (values.size) {
            0 -> null
            1 -> {
                val value = values.single()
                if (!dataConversionService.isValid(value)) {
                    throw DataConversionException("Cannot convert - value is malformed.")
                }
                dataConversionService.fromString(value)
            }
            else -> throw DataConversionException("Cannot convert - multiple values specified.")
        }
    }

    override fun toValues(value: Any?): List<String> {
        value ?: return emptyList()
        if (value::class != dataConversionService.clazz) {
            throw DataConversionException("Cannot convert - value is not a ${dataConversionService.clazz.simpleName}.")
        }
        // Not sure why the function signature is Any?. If this cast ever causes an issue, remove
        // the suppression and fix the issue, but for now we'll leave it.
        @Suppress("UNCHECKED_CAST", "UnsafeCast")
        return listOf(dataConversionService.toString(value as T))
    }
}


package com.piperframework.util

import com.piperframework.dataConversion.DataConversionException
import com.piperframework.dataConversion.DataConversionService
import io.ktor.util.ConversionService
import java.lang.reflect.Type

fun <T : Any> conversionService(dataConversionService: DataConversionService<T>) = object : ConversionService {
  override fun fromValues(values: List<String>, type: Type): Any? {
    return when (values.size) {
        0 -> null
        1 -> {
            val value = values.single()
            if (!dataConversionService.isValid(value)) {
                throw DataConversionException(null, dataConversionService.kClass)
            }
            dataConversionService.fromString(value)
        }
        else -> throw DataConversionException(null, dataConversionService.kClass)
    }
  }

  override fun toValues(value: Any?): List<String> {
    value ?: return emptyList()
    if (value::class != dataConversionService.kClass) {
      throw DataConversionException(null, dataConversionService.kClass)
    }
    // Not sure why the function signature is Any?. If this cast ever causes an issue, remove
    // the suppression and fix the issue, but for now we'll leave it.
    @Suppress("UNCHECKED_CAST", "UnsafeCast")
    return listOf(dataConversionService.toString(value as T))
  }
}

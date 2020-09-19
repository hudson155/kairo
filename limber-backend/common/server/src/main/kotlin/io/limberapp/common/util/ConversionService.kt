package io.limberapp.common.util

import io.ktor.util.ConversionService
import io.limberapp.typeConversion.TypeConversionService
import io.limberapp.typeConversion.exception.TypeConversionException
import java.lang.reflect.Type

fun <T : Any> conversionService(typeConversionService: TypeConversionService<T>) = object : ConversionService {
  override fun fromValues(values: List<String>, type: Type): Any? {
    return when (values.size) {
      0 -> null
      1 -> {
        val value = values.single()
        if (!typeConversionService.isValid(value)) {
          throw TypeConversionException(null, typeConversionService.kClass)
        }
        typeConversionService.fromString(value)
      }
      else -> throw TypeConversionException(null, typeConversionService.kClass)
    }
  }

  override fun toValues(value: Any?): List<String> {
    value ?: return emptyList()
    if (value::class != typeConversionService.kClass) {
      throw TypeConversionException(null, typeConversionService.kClass)
    }
    // Not sure why the function signature is Any?. If this cast ever causes an issue, remove
    // the suppression and fix the issue, but for now we'll leave it.
    @Suppress("UNCHECKED_CAST", "UnsafeCast")
    return listOf(typeConversionService.toString(value as T))
  }
}

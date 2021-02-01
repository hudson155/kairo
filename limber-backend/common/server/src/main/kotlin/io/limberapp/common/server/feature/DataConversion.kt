package io.limberapp.common.server.feature

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.DataConversion
import io.ktor.util.ConversionService
import io.limberapp.common.typeConversion.TypeConverter
import io.limberapp.common.typeConversion.exception.TypeConversionException
import java.lang.reflect.Type

internal fun Application.configureDataConversion(typeConverters: Set<TypeConverter<*>>) {
  install(DataConversion) {
    typeConverters.forEach { convert(it.kClass, conversionService(it)) }
  }
}

internal fun <T : Any> conversionService(
    typeConverter: TypeConverter<T>,
): ConversionService =
    object : ConversionService {
      override fun fromValues(values: List<String>, type: Type): Any? {
        if (type !== typeConverter.kClass.javaObjectType) {
          throw TypeConversionException(typeConverter.kClass, "Type was $type.")
        }
        return when (values.size) {
          0 -> null
          1 -> {
            val value = values.single()
            if (typeConverter.isValid(value) == false) {
              throw TypeConversionException(typeConverter.kClass, "Value was invalid.")
            }
            typeConverter.parseString(value)
          }
          else -> throw TypeConversionException(typeConverter.kClass, "Multiple values provided.")
        }
      }

      override fun toValues(value: Any?): List<String> {
        value ?: return emptyList()
        if (value::class != typeConverter.kClass) {
          throw TypeConversionException(typeConverter.kClass, "Class was ${value::class}.")
        }
        // Not sure why the function signature is Any?. If this cast ever causes an issue, remove
        // the suppression and fix the issue, but for now we'll leave it.
        @Suppress("UNCHECKED_CAST", "UnsafeCast")
        return listOf(typeConverter.writeString(value as T))
      }
    }

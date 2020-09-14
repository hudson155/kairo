package io.limberapp.common.dataConversion.conversionService

import io.limberapp.common.dataConversion.DataConversionService
import io.limberapp.common.validator.Validator

actual object UuidConversionService : DataConversionService<String> {
  override val kClass = String::class

  override fun isValid(value: String) = Validator.uuid(value) || Validator.base64EncodedUuid(value)

  override fun fromString(value: String): String = when {
    Validator.uuid(value) -> value
    Validator.base64EncodedUuid(value) -> value
    else -> error("Invalid UUID $value")
  }

  override fun toString(value: String) = value
}

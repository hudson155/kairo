package io.limberapp.common.dataConversion.conversionService

import io.limberapp.common.dataConversion.DataConversionService
import io.limberapp.common.util.uuid.uuidFromBase64Encoded
import io.limberapp.common.validator.Validator
import java.util.*

object UuidConversionService : DataConversionService<UUID> {
  override val kClass = UUID::class

  override fun isValid(value: String) = Validator.uuid(value) || Validator.base64EncodedUuid(value)

  override fun fromString(value: String): UUID = when {
    Validator.uuid(value) -> UUID.fromString(value)
    Validator.base64EncodedUuid(value) -> uuidFromBase64Encoded(value)
    else -> error("Invalid UUID $value")
  }

  override fun toString(value: UUID) = value.toString()
}

package io.limberapp.common.typeConversion.conversionService

import io.limberapp.common.logging.LoggerFactory
import io.limberapp.common.typeConversion.TypeConversionService
import io.limberapp.common.util.uuid.uuidFromBase64Encoded
import io.limberapp.common.util.uuid.uuidFromString
import io.limberapp.common.validation.Validator
import java.util.UUID

object UuidConversionService : TypeConversionService<UUID> {
  private val logger = LoggerFactory.getLogger(UuidConversionService::class)

  override val kClass = UUID::class

  override fun isValid(value: String) = Validator.uuid(value) || Validator.base64EncodedUuid(value)

  override fun parseString(value: String): UUID = when {
    Validator.uuid(value) -> run {
      logger.debug("Decoding UUID.")
      return@run uuidFromString(value)
    }
    Validator.base64EncodedUuid(value) -> run {
      logger.debug("Decoding base 64 encoded UUID.")
      return@run uuidFromBase64Encoded(value)
    }
    else -> error("Invalid UUID $value")
  }

  override fun writeString(value: UUID) = value.toString()
}

package io.limberapp.typeConversion.typeConverter

import io.limberapp.typeConversion.TypeConverter
import io.limberapp.util.uuid.uuidFromBase64Encoded
import io.limberapp.util.uuid.uuidFromString
import io.limberapp.validation.Validator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.UUID
import kotlin.reflect.KClass

object UuidTypeConverter : TypeConverter<UUID> {
  private val logger: Logger = LoggerFactory.getLogger(UuidTypeConverter::class.java)

  override val kClass: KClass<UUID> = UUID::class

  override fun isValid(value: String): Boolean =
      Validator.uuid(value) || Validator.base64EncodedUuid(value)

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

  override fun writeString(value: UUID): String = value.toString()
}

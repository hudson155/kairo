package com.piperframework.dataConversion.conversionService

import com.piperframework.dataConversion.DataConversionService
import com.piperframework.util.uuid.uuidFromBase64Encoded
import com.piperframework.validator.Validator
import java.util.UUID

/**
 * In the JVM, UUIDs use the [java.util.UUID] class.
 */
actual object UuidConversionService : DataConversionService<UUID> {

    override val kClass = UUID::class

    override fun isValid(value: String) = Validator.uuid(value) || Validator.base64EncodedUuid(value)

    override fun fromString(value: String): UUID = when {
        Validator.uuid(value) -> UUID.fromString(value)
        Validator.base64EncodedUuid(value) -> uuidFromBase64Encoded(value)
        else -> error("Invalid UUID $value")
    }

    override fun toString(value: UUID) = value.toString()
}

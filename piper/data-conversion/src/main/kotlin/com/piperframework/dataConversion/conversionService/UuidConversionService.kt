package com.piperframework.dataConversion.conversionService

import com.piperframework.dataConversion.DataConversionService
import com.piperframework.util.uuid.uuidFromBase64Encoded
import com.piperframework.validator.Validator
import java.util.UUID

class UuidConversionService : DataConversionService<UUID> {

    override val clazz = UUID::class

    override fun isValid(value: String) = Validator.uuid(value) || Validator.base64EncidedUuid(value)

    override fun fromString(value: String): UUID = when {
        Validator.uuid(value) -> UUID.fromString(value)
        Validator.base64EncidedUuid(value) -> uuidFromBase64Encoded(value)
        else -> error("Invalid UUID $value")
    }

    override fun toString(value: UUID) = value.toString()
}

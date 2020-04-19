package com.piperframework.dataConversion.conversionService

import com.piperframework.dataConversion.DataConversionService
import com.piperframework.validator.Validator

/**
 * In JS, UUIDs use the [String] class instead of an actual UUID class. UUID validation is still performed by the
 * conversion service.
 */
actual object UuidConversionService : DataConversionService<String> {

    override val klass = String::class

    override fun isValid(value: String) = Validator.uuid(value) || Validator.base64EncodedUuid(value)

    override fun fromString(value: String): String = when {
        Validator.uuid(value) -> value
        Validator.base64EncodedUuid(value) -> value
        else -> error("Invalid UUID $value")
    }

    override fun toString(value: String) = value
}

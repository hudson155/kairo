package io.limberapp.framework.dataConversion.conversionService

import io.limberapp.framework.dataConversion.DataConversionService
import io.limberapp.framework.validator.Validator
import java.util.UUID

class UuidConversionService : DataConversionService<UUID> {
    override val clazz = UUID::class
    override fun isValid(value: String) = Validator.uuid(value)
    override fun fromString(value: String): UUID = UUID.fromString(value)
    override fun toString(value: UUID) = value.toString()
}

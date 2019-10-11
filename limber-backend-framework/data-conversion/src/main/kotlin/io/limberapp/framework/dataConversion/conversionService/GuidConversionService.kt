package io.limberapp.framework.dataConversion.conversionService

import io.limberapp.framework.dataConversion.SimpleConversionService
import io.limberapp.framework.validation.validator.Validator
import java.util.UUID

class GuidConversionService : SimpleConversionService<UUID>(UUID::class) {
    override fun isValid(value: String) = Validator.guid(value)
    override fun fromValue(value: String): UUID = UUID.fromString(value)
    override fun toValue(value: UUID) = value.toString()
}

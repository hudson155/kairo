package com.piperframework.dataConversion.conversionService

import com.piperframework.dataConversion.DataConversionService

object RegexConversionService : DataConversionService<Regex> {
    override val kClass = Regex::class

    override fun isValid(value: String) = true // Impossible to validate Regex statically.

    override fun fromString(value: String) = Regex(value)

    override fun toString(value: Regex) = value.pattern
}

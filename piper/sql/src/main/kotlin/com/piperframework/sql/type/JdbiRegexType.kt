package com.piperframework.sql.type

import com.piperframework.dataConversion.conversionService.RegexConversionService
import com.piperframework.sql.ConversionServiceJdbiType

internal object JdbiRegexType : ConversionServiceJdbiType<Regex>(Regex::class) {
    override val columnMapper = object : ConversionServiceColumnMapper<Regex>(RegexConversionService) {}
    override val argumentFactory = object : ConversionServiceArgumentFactory<Regex>(RegexConversionService) {}
}

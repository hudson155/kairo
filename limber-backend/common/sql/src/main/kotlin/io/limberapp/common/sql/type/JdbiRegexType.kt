package io.limberapp.common.sql.type

import io.limberapp.common.dataConversion.conversionService.RegexConversionService
import io.limberapp.common.sql.ConversionServiceJdbiType

internal object JdbiRegexType : ConversionServiceJdbiType<Regex>(Regex::class) {
  override val columnMapper = object : ConversionServiceColumnMapper<Regex>(RegexConversionService) {}
  override val argumentFactory = object : ConversionServiceArgumentFactory<Regex>(RegexConversionService) {}
}

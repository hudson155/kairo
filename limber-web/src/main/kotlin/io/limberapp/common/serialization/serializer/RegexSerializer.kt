package io.limberapp.common.serialization.serializer

import io.limberapp.common.dataConversion.conversionService.RegexConversionService
import io.limberapp.common.serialization.ConversionServiceSerializer

object RegexSerializer
  : ConversionServiceSerializer<Regex>("Regex", RegexConversionService)

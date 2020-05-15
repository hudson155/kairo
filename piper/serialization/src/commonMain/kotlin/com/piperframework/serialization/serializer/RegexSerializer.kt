package com.piperframework.serialization.serializer

import com.piperframework.dataConversion.conversionService.RegexConversionService
import com.piperframework.serialization.ConversionServiceSerializer

object RegexSerializer
  : ConversionServiceSerializer<Regex>("Regex", RegexConversionService)

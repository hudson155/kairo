package io.limberapp.common.serialization.serializer

import io.limberapp.common.dataConversion.conversionService.LocalDateTimeConversionService
import io.limberapp.common.serialization.ConversionServiceSerializer
import io.limberapp.common.types.LocalDateTime

object LocalDateTimeSerializer
  : ConversionServiceSerializer<LocalDateTime>("LocalDateTime", LocalDateTimeConversionService)

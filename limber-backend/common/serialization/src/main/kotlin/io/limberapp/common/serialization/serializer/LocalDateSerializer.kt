package io.limberapp.common.serialization.serializer

import io.limberapp.common.dataConversion.conversionService.LocalDateConversionService
import io.limberapp.common.serialization.ConversionServiceSerializer
import io.limberapp.common.types.LocalDate

object LocalDateSerializer
  : ConversionServiceSerializer<LocalDate>("LocalDate", LocalDateConversionService)

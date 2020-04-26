package com.piperframework.serialization.serializer

import com.piperframework.dataConversion.conversionService.LocalDateConversionService
import com.piperframework.serialization.ConversionServiceSerializer
import com.piperframework.types.LocalDate

object LocalDateSerializer
    : ConversionServiceSerializer<LocalDate>("LocalDate", LocalDateConversionService)

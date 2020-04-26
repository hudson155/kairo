package com.piperframework.serialization.serializer

import com.piperframework.dataConversion.conversionService.LocalDateTimeConversionService
import com.piperframework.serialization.ConversionServiceSerializer
import com.piperframework.types.LocalDateTime

object LocalDateTimeSerializer
    : ConversionServiceSerializer<LocalDateTime>("LocalDateTime", LocalDateTimeConversionService)

package com.piperframework.serialization

import com.piperframework.dataConversion.conversionService.UuidConversionService
import com.piperframework.types.UUID

object UuidSerializer : ConversionServiceSerializer<UUID>("UUID", UuidConversionService)

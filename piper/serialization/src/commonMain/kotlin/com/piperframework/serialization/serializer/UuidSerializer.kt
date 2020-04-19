package com.piperframework.serialization.serializer

import com.piperframework.dataConversion.conversionService.UuidConversionService
import com.piperframework.serialization.ConversionServiceSerializer
import com.piperframework.types.UUID

object UuidSerializer : ConversionServiceSerializer<UUID>("UUID", UuidConversionService)

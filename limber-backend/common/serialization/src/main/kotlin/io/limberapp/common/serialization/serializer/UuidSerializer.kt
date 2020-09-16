package io.limberapp.common.serialization.serializer

import io.limberapp.common.dataConversion.conversionService.UuidConversionService
import io.limberapp.common.serialization.ConversionServiceSerializer
import io.limberapp.common.types.UUID

object UuidSerializer
  : ConversionServiceSerializer<UUID>("UUID", UuidConversionService)

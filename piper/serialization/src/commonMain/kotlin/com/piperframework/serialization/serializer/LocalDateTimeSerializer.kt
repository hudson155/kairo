package com.piperframework.serialization.serializer

import com.piperframework.types.LocalDateTime
import kotlinx.serialization.KSerializer

expect object LocalDateTimeSerializer : KSerializer<LocalDateTime>

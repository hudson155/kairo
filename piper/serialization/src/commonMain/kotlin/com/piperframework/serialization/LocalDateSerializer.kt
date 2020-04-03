package com.piperframework.serialization

import com.piperframework.types.LocalDate
import kotlinx.serialization.KSerializer

expect object LocalDateSerializer : KSerializer<LocalDate>

package com.piperframework.serialization.serializer

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind

/**
 * In JS, LocalDateTimes use the [String] class instead of an actual LocalDateTime class.
 */
actual object LocalDateTimeSerializer : KSerializer<String> {
    override val descriptor = PrimitiveDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) =
        encoder.encodeString(value)

    override fun deserialize(decoder: Decoder): String =
        decoder.decodeString()
}

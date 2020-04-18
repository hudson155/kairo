package com.piperframework.serialization.serializer

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind

actual object LocalDateSerializer : KSerializer<String> {

    override val descriptor = PrimitiveDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) =
        encoder.encodeString(value)

    override fun deserialize(decoder: Decoder): String =
        decoder.decodeString()
}

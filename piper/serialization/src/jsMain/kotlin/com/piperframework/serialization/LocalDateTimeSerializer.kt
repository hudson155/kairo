package com.piperframework.serialization

import com.piperframework.types.LocalDateTime
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind

actual object LocalDateTimeSerializer : KSerializer<String> {

    override val descriptor = PrimitiveDescriptor(LocalDateTime::class.simpleName!!, PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }

    override fun deserialize(decoder: Decoder): String {
        return decoder.decodeString()
    }
}
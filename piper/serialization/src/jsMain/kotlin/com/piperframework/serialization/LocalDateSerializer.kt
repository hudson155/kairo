package com.piperframework.serialization

import com.piperframework.types.LocalDate
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind

actual object LocalDateSerializer : KSerializer<String> {

    override val descriptor = PrimitiveDescriptor(LocalDate::class.simpleName!!, PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }

    override fun deserialize(decoder: Decoder): String {
        return decoder.decodeString()
    }
}

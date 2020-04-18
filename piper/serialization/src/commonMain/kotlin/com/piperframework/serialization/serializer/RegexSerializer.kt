package com.piperframework.serialization.serializer

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind

object RegexSerializer : KSerializer<Regex> {

    override val descriptor = PrimitiveDescriptor("Regex", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Regex) =
        encoder.encodeString(value.pattern)

    override fun deserialize(decoder: Decoder): Regex =
        Regex(decoder.decodeString())
}

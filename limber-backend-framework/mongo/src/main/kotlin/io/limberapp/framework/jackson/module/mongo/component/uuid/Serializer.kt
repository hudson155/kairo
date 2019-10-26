package io.limberapp.framework.jackson.module.mongo.component.uuid

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import io.limberapp.framework.util.asByteArray
import java.util.Base64
import java.util.UUID

internal class Serializer : StdSerializer<UUID>(UUID::class.java) {

    override fun serialize(value: UUID, gen: JsonGenerator, provider: SerializerProvider) {
        gen.apply {
            writeStartObject()
            writeStringField(BINARY_KEY, stringValue(value))
            writeStringField(TYPE_KEY, TYPE_VALUE)
            writeEndObject()
        }
    }

    private fun stringValue(value: UUID): String? {
        val byteArray = value.asByteArray()
        return Base64.getEncoder().encodeToString(byteArray)
    }
}

package io.limberapp.framework.jackson.module.mongo.component.long

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

internal class Serializer : StdSerializer<Long>(Long::class.java) {

    override fun serialize(value: Long, gen: JsonGenerator, provider: SerializerProvider) {
        gen.apply {
            writeStartObject()
            writeStringField(NUMBER_LONG_KEY, stringValue(value))
            writeEndObject()

        }
    }

    private fun stringValue(value: Long) = value.toString()
}

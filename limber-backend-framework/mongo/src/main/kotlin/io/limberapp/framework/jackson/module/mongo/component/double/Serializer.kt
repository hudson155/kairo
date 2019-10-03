package io.limberapp.framework.jackson.module.mongo.component.double

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

class Serializer : StdSerializer<Double>(Double::class.java) {

    override fun serialize(value: Double, gen: JsonGenerator, provider: SerializerProvider) {
        gen.apply {
            writeStartObject()
            writeStringField(NUMBER_DECIMAL_KEY, stringValue(value))
            writeEndObject()
        }
    }

    private fun stringValue(value: Double) = value.toString()
}

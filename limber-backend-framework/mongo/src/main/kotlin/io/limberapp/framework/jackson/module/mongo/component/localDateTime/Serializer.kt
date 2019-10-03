package io.limberapp.framework.jackson.module.mongo.component.localDateTime

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class Serializer : StdSerializer<LocalDateTime>(LocalDateTime::class.java) {

    override fun serialize(value: LocalDateTime, gen: JsonGenerator, provider: SerializerProvider) {
        gen.apply {
            writeStartObject()
            writeNumberField(DATE_KEY, longValue(value))
            writeEndObject()
        }
    }

    private fun longValue(value: LocalDateTime): Long {
        val millisFromSeconds = value.toEpochSecond(ZoneOffset.UTC) * MILLIS_PER_SECOND
        val millisFromNanos = value.nano / NANOS_PER_MILLI
        return millisFromSeconds + millisFromNanos
    }
}

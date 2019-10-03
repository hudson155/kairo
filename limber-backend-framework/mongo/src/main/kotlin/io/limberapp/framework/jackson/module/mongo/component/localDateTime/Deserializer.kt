package io.limberapp.framework.jackson.module.mongo.component.localDateTime

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class Deserializer : StdDeserializer<LocalDateTime>(LocalDateTime::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LocalDateTime {
        val tree = p.readValueAsTree<JsonNode>()
        return tree.date()
    }

    private fun JsonNode.date(): LocalDateTime {
        val longValue = this[DATE_KEY].longValue()
        return LocalDateTime.ofEpochSecond(
            longValue / MILLIS_PER_SECOND,
            (longValue % MILLIS_PER_SECOND).toInt() * NANOS_PER_MILLI,
            ZoneOffset.UTC
        )
    }
}

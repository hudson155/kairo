package io.limberapp.framework.jackson.module.mongo.component.long

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

class Deserializer : StdDeserializer<Long>(Long::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Long {
        val tree = p.readValueAsTree<JsonNode>()
        return tree.numberLong()
    }

    private fun JsonNode.numberLong(): Long {
        val textValue = this[NUMBER_LONG_KEY].textValue()
        return textValue.toLong()
    }
}

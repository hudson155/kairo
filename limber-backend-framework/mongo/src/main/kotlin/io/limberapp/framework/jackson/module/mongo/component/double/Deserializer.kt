package io.limberapp.framework.jackson.module.mongo.component.double

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

internal class Deserializer : StdDeserializer<Double>(Double::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Double {
        val tree = p.readValueAsTree<JsonNode>()
        return tree.numberDecimal()
    }

    private fun JsonNode.numberDecimal(): Double {
        val textValue = this[NUMBER_DECIMAL_KEY].textValue()
        return textValue.toDouble()
    }
}

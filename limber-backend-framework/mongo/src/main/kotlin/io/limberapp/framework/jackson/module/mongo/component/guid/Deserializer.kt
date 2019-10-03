package io.limberapp.framework.jackson.module.mongo.component.guid

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import io.limberapp.framework.util.UUID
import java.util.Base64
import java.util.UUID

internal class Deserializer : StdDeserializer<UUID>(UUID::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): UUID {
        val tree = p.readValueAsTree<JsonNode>()
        return tree.binary()
    }

    private fun JsonNode.binary(): UUID {
        check(this[TYPE_KEY].textValue() == TYPE_VALUE)
        val stringValue = this[BINARY_KEY].textValue()
        val byteArray = Base64.getDecoder().decode(stringValue)
        return UUID(byteArray)
    }
}

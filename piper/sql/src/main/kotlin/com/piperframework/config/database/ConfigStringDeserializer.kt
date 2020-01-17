package com.piperframework.config.database

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.piperframework.config.ConfigString

class ConfigStringDeserializer : JsonDeserializer<String>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): String {
        val node = p.codec.readTree<JsonNode>(p)
        val type = ConfigString.Type.valueOf(checkNotNull(node["type"].textValue()))
        val plaintextValue = checkNotNull(node["value"].textValue())
        return when (type) {
            ConfigString.Type.PLAINTEXT -> plaintextValue
            ConfigString.Type.ENVIRONMENT_VARIABLE -> checkNotNull(System.getenv(plaintextValue))
        }
    }
}

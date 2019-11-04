package io.limberapp.framework.mongo.collection

import io.limberapp.framework.store.LimberMongoObjectMapper
import org.bson.Document
import org.bson.conversions.Bson

data class Component(
    private val contents: MutableMap<String, Any> = mutableMapOf()
) {

    private val objectMapper = LimberMongoObjectMapper()

    fun isNotEmpty() = contents.isNotEmpty()

    fun containsKey(key: String) = contents.containsKey(key)

    operator fun plusAssign(map: Map<String, Any>) {
        map.forEach { this[it.key] = it.value }
    }

    operator fun set(key: String, value: Any) {
        check(!contents.containsKey(key))
        contents[key] = value
    }

    fun asBson(): Bson {
        return Document.parse(objectMapper.writeValueAsString(contents))
    }
}

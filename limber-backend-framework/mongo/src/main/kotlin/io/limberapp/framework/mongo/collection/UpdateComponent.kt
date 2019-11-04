package io.limberapp.framework.mongo.collection

import io.limberapp.framework.store.LimberMongoObjectMapper
import org.bson.Document

data class UpdateComponent(val delegate: Document = Document()) {

    private val objectMapper = LimberMongoObjectMapper()

    operator fun plusAssign(map: Map<String, Any>) {
        map.forEach { this[it.key] = it.value }
    }

    operator fun set(key: String, value: Any) {
        check(!delegate.containsKey(key))
        delegate[key] = when (value) {
            is Boolean -> value
            is Number -> value
            is String -> value
            else -> Document.parse(objectMapper.writeValueAsString(value))
        }
    }
}

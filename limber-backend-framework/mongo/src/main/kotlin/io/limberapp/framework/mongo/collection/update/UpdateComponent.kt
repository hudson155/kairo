package io.limberapp.framework.mongo.collection.update

import io.limberapp.framework.store.LimberMongoObjectMapper
import org.bson.Document

class UpdateComponent {

    private val objectMapper = LimberMongoObjectMapper()

    var delegate = Document()
        private set

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

package io.limberapp.framework.mongo.collection

import io.limberapp.framework.store.LimberMongoObjectMapper
import org.bson.Document
import org.bson.conversions.Bson

class Update {

    private val objectMapper = LimberMongoObjectMapper()

    private var set: Document = Document()
    private var push = Document()

    fun setDocument(document: Any) {
        check(set.isEmpty())
        set = Document.parse(objectMapper.writeValueAsString(document))
    }

    fun setDocument(key: String, value: Any) {
        check(!set.containsKey(key))
        set[key] = Document.parse(objectMapper.writeValueAsString(value))
    }

    fun set(key: String, value: Boolean) {
        check(!set.containsKey(key))
        set[key] = value
    }

    fun pushDocument(document: Any) {
        check(push.isEmpty())
        push = Document.parse(objectMapper.writeValueAsString(document))
    }

    fun pushDocument(key: String, value: Any) {
        check(!push.containsKey(key))
        push[key] = Document.parse(objectMapper.writeValueAsString(value))
    }

    fun push(key: String, value: Boolean) {
        check(!push.containsKey(key))
        push[key] = value
    }

    fun asBson(): Bson {
        check(set.isNotEmpty() || push.isNotEmpty())
        return Document().apply {
            if (set.isNotEmpty()) this["\$set"] = set
            if (push.isNotEmpty()) this["\$push"] = push
        }
    }
}

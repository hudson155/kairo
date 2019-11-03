package io.limberapp.framework.mongo.collection

import org.bson.Document
import org.bson.conversions.Bson

class Update {

    private var set: Document = Document()
    private var push = Document()

    fun set(document: Document) {
        check(set.isEmpty())
        set = document
    }

    fun push(key: String, value: Any) {
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

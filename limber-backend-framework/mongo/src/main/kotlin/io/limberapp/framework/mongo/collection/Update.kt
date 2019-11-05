package io.limberapp.framework.mongo.collection

import org.bson.Document
import org.bson.conversions.Bson

data class Update(
    val set: Component = Component(),
    val push: Component = Component(),
    val pull: Component = Component()
) {

    fun asBson(): Bson {
        check(listOf(set, push, pull).any { it.isNotEmpty() })
        return Document().apply {
            if (set.isNotEmpty()) this["\$set"] = set.asBson()
            if (push.isNotEmpty()) this["\$push"] = push.asBson()
            if (pull.isNotEmpty()) this["\$pull"] = pull.asBson()
        }
    }
}

package io.limberapp.framework.mongo.collection.update

import org.bson.Document
import org.bson.conversions.Bson

data class Update(
    val set: UpdateComponent = UpdateComponent(),
    val push: UpdateComponent = UpdateComponent(),
    val pull: UpdateComponent = UpdateComponent()
) {

    fun asBson(): Bson {
        check(listOf(set, push, pull).any { it.delegate.isNotEmpty() })
        return Document().apply {
            if (set.delegate.isNotEmpty()) this["\$set"] = set.delegate
            if (push.delegate.isNotEmpty()) this["\$push"] = push.delegate
            if (pull.delegate.isNotEmpty()) this["\$pull"] = pull.delegate
        }
    }
}

package io.limberapp.framework.store

import com.mongodb.client.MongoCollection
import org.bson.Document
import org.bson.conversions.Bson

internal fun MongoCollection<Document>.findOne(filter: Bson): Document? {
    with(find(filter)) {
        val iterator = iterator()
        if (!iterator.hasNext()) return null
        val single = iterator.next()
        check(!iterator.hasNext())
        return single
    }
}

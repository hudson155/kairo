package io.limberapp.framework.mongo.collection

import com.mongodb.client.model.Filters
import org.bson.Document
import org.bson.conversions.Bson

data class FindFilter(
    private val eq: Document = Document()
) {

    fun eq(key: String, value: Any) {
        check(!eq.containsKey(key))
        eq[key] = value
    }

    fun asBson(): Bson {
        check(eq.isNotEmpty())
        val filters = eq.map { Filters.eq(it.key, it.value) }.toMutableList()
        if (!eq.containsKey("deleted")) filters.add(
            Filters.ne(
                "deleted",
                true
            )
        )
        return Filters.and(filters)
    }
}

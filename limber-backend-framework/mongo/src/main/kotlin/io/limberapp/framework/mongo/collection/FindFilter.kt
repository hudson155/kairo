package io.limberapp.framework.mongo.collection

import com.mongodb.client.model.Filters
import org.bson.conversions.Bson

class FindFilter {

    val eq = Component()

    fun asBson(): Bson {
        check(eq.isNotEmpty())
        val filters = mutableListOf(eq.asBson()).apply {
            if (!eq.containsKey("deleted")) add(Filters.ne("deleted", true))
        }
        return Filters.and(filters)
    }
}

package io.limberapp.framework.mongo.collection

import com.mongodb.client.model.Filters
import org.bson.conversions.Bson

data class FindFilter(
    val eq: Component = Component()
) {

    fun asBson(): Bson {
        check(eq.isNotEmpty())
        val filters = mutableListOf(eq.asBson()).apply {
            if (!eq.containsKey("deleted")) add(Filters.ne("deleted", true))
        }
        return Filters.and(filters)
    }

    fun applyUpdate(update: Update): FindFilter {
        return copy().apply {
            update.pull.forEach {
                check(!eq.containsKey(it.key))
//                eq[it.key] = it.value // TODO: First, need to have a map without nested objects
            }
        }
    }
}

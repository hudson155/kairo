package io.limberapp.framework.store

import com.fasterxml.jackson.databind.PropertyNamingStrategy

private const val ID_KEY = "id"
private const val MONGO_ID_KEY = "_id"

/**
 * MongoDB uses _id, whereas Limber uses id. This handles that discrepancy.
 */
class LimberMongoPropertyNamingStrategy : PropertyNamingStrategy.PropertyNamingStrategyBase() {
    override fun translate(propertyName: String): String {
        if (propertyName == ID_KEY) return MONGO_ID_KEY
        return propertyName
    }
}

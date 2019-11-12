package io.limberapp.framework.store

import io.limberapp.framework.jackson.module.mongo.MongoModule
import io.limberapp.framework.jackson.objectMapper.LimberObjectMapper

class LimberMongoObjectMapper : LimberObjectMapper() {

    init {
        registerMongoModule()
        configurePropertyNaming()
    }

    private fun registerMongoModule() {
        registerModule(MongoModule())
    }

    private fun configurePropertyNaming() {
        propertyNamingStrategy = LimberMongoPropertyNamingStrategy()
    }
}

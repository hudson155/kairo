package io.limberapp.framework.store

import io.limberapp.framework.jackson.module.mongo.MongoModule
import io.limberapp.framework.jackson.objectMapper.LimberObjectMapper

internal class LimberMongoObjectMapper : LimberObjectMapper(prettyPrint = false) {

    init {
        registerMongoModule()
    }

    private fun registerMongoModule() {
        registerModule(MongoModule())
    }
}

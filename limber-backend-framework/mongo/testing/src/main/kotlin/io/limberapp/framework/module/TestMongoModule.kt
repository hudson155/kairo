package io.limberapp.framework.module

import io.limberapp.framework.config.database.MongoDatabaseConfig
import io.limberapp.framework.createClient

class TestMongoModule : MongoModule(MongoDatabaseConfig.local("limberapptest")) {

    fun dropDatabase() {
        val mongoClient = mongoDatabaseConfig.createClient()
        mongoClient.getDatabase(mongoDatabaseConfig.database).drop()
    }
}

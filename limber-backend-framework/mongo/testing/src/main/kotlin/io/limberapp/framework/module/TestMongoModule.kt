package io.limberapp.framework.module

import io.limberapp.framework.config.database.DatabaseConfig
import io.limberapp.framework.createClient

class TestMongoModule : MongoModule(DatabaseConfig.local("limberapptest")) {

    fun dropDatabase() {
        val mongoClient = databaseConfig.createClient()
        mongoClient.getDatabase(databaseConfig.database).drop()
    }
}

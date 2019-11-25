package com.piperframework.module

import com.piperframework.config.database.MongoDatabaseConfig
import com.piperframework.createClient

class TestMongoModule : MongoModule(MongoDatabaseConfig.local("limberapptest")) {

    fun dropDatabase() {
        val mongoClient = mongoDatabaseConfig.createClient()
        mongoClient.getDatabase(mongoDatabaseConfig.database).drop()
    }
}

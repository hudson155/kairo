package com.piperframework.module

import com.piperframework.config.database.MongoDatabaseConfig
import com.piperframework.createClient

class TestMongoModule : MongoModule(MongoDatabaseConfig.local("piperapptest")) {

    fun dropDatabase() {
        val mongoClient = mongoDatabaseConfig.createClient()
        mongoClient.getDatabase(mongoDatabaseConfig.database).drop()
    }
}

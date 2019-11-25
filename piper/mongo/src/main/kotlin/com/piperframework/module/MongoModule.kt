package com.piperframework.module

import com.google.inject.AbstractModule
import com.mongodb.client.MongoDatabase
import com.piperframework.config.database.MongoDatabaseConfig
import com.piperframework.createClient

/**
 * MongoModule configures bindings for MongoDB.
 */
open class MongoModule(protected val mongoDatabaseConfig: MongoDatabaseConfig) : AbstractModule() {

    override fun configure() {
        bind(MongoDatabase::class.java)
            .toInstance(mongoDatabaseConfig.createClient().getDatabase(mongoDatabaseConfig.database))
    }
}

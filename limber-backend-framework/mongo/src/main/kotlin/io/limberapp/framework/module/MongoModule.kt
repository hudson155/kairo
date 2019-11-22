package io.limberapp.framework.module

import com.google.inject.AbstractModule
import com.mongodb.client.MongoDatabase
import io.limberapp.framework.config.database.MongoDatabaseConfig
import io.limberapp.framework.createClient

/**
 * MongoModule configures bindings for MongoDB.
 */
open class MongoModule(protected val mongoDatabaseConfig: MongoDatabaseConfig) : AbstractModule() {

    override fun configure() {
        bind(MongoDatabase::class.java)
            .toInstance(mongoDatabaseConfig.createClient().getDatabase(mongoDatabaseConfig.database))
    }
}

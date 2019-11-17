package io.limberapp.framework.module

import com.google.inject.AbstractModule
import com.mongodb.client.MongoDatabase
import io.limberapp.framework.config.database.DatabaseConfig
import io.limberapp.framework.createClient

/**
 * MongoModule configures bindings for MongoDB.
 */
open class MongoModule(
    protected val databaseConfig: DatabaseConfig
) : AbstractModule() {

    override fun configure() {
        bind(MongoDatabase::class.java)
            .toInstance(databaseConfig.createClient().getDatabase(databaseConfig.database))
    }
}

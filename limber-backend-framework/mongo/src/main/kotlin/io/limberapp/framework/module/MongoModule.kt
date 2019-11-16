package io.limberapp.framework.module

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.mongodb.client.MongoDatabase
import io.limberapp.framework.config.database.DatabaseConfig
import io.limberapp.framework.createClient

/**
 * MongoModule configures bindings for MongoDB.
 */
class MongoModule(
    private val databaseConfig: DatabaseConfig
) : AbstractModule() {

    override fun configure() = Unit

    @Provides
    @Singleton
    fun mongoDatabase(): MongoDatabase {
        val mongoClient = databaseConfig.createClient()
        return mongoClient.getDatabase(databaseConfig.database)
    }
}

package io.limberapp.backend.module.orgs.endpoint

import com.google.inject.Provides
import com.google.inject.Singleton
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.WriteConcern
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import io.ktor.application.Application
import io.limberapp.framework.MainModule
import io.limberapp.framework.config.Config

/**
 * TestModule configures bindings required for tests that are not directly bound in the module being
 * tested.
 *
 * TODO: This class has duplicated stuff from MainModuleImpl
 */
internal class TestMainModule(
    application: Application,
    config: Config
) : MainModule(application, config) {

    @Provides
    @Singleton
    fun mongoClient(config: Config): MongoClient {
        val connectionString = with(config.database) { "mongodb+srv://$user:$password@$host" }
        val clientSettings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(connectionString))
            .writeConcern(WriteConcern.MAJORITY)
            .build()
        return MongoClients.create(clientSettings)
    }

    @Provides
    @Singleton
    fun mongoDatabase(config: Config, mongoClient: MongoClient): MongoDatabase =
        mongoClient.getDatabase(config.database.database)
}

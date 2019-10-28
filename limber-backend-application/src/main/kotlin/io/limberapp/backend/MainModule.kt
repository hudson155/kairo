package io.limberapp.backend

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.WriteConcern
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import io.ktor.application.Application
import io.limberapp.backend.config.Config

/**
 * MainModule configures bindings for classes that are not related to a specific application module.
 */
internal class MainModule(
    private val application: Application,
    private val config: Config
) : AbstractModule() {

    override fun configure() {
        bind(Config::class.java).toInstance(config)
        bind(Application::class.java).toInstance(application)
    }

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

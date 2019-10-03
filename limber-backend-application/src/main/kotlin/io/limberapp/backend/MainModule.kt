package io.limberapp.backend

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.WriteConcern
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import com.typesafe.config.ConfigFactory
import io.ktor.application.Application
import io.limberapp.backend.config.Config
import io.limberapp.backend.config.database.DatabaseConfig

/**
 * MainModule configures bindings for classes that are not related to a specific application module.
 */
internal class MainModule(private val application: Application) : AbstractModule() {

    override fun configure() {
        bind(Application::class.java).toInstance(application)
    }

    @Provides
    @Singleton
    fun config(): Config {
        return with(ConfigFactory.load()) {
            Config(
                database = DatabaseConfig(
                    host = getString("database.host"),
                    database = getString("database.database"),
                    user = getString("database.user"),
                    password = getString("database.password")
                )
            )
        }
    }

    @Provides
    @Singleton
    fun mongoDatabase(config: Config): MongoDatabase {
        val connectionString = with(config.database) { "mongodb+srv://$user:$password@$host" }
        val clientSettings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(connectionString))
            .retryWrites(true)
            .writeConcern(WriteConcern.MAJORITY)
            .build()
        val client = MongoClients.create(clientSettings)
        return client.getDatabase(config.database.database)
    }
}

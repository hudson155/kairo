package io.limberapp.backend.module.users.testing

import com.google.inject.Provides
import com.google.inject.Singleton
import com.mongodb.client.MongoDatabase
import io.ktor.application.Application
import io.limberapp.framework.MainModule
import io.limberapp.framework.config.Config
import io.limberapp.framework.config.database.DatabaseConfig
import io.limberapp.framework.createClient
import io.limberapp.framework.util.uuidGenerator.UuidGenerator
import java.time.Clock

internal class TestMainModule(
    application: Application,
    clock: Clock,
    uuidGenerator: UuidGenerator,
    config: Config
) : MainModule(application, clock, config, uuidGenerator) {

    @Provides
    @Singleton
    fun mongoDatabase(databaseConfig: DatabaseConfig): MongoDatabase {
        val mongoClient = databaseConfig.createClient()
        return mongoClient.getDatabase(databaseConfig.database)
    }
}

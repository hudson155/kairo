package io.limberapp.backend.module.orgs.testing

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
    config: Config,
    uuidGenerator: UuidGenerator
) : MainModule(application, clock, config, uuidGenerator) {

    @Provides
    @Singleton
    fun mongoDatabase(databseConfig: DatabaseConfig): MongoDatabase {
        val mongoClient = databseConfig.createClient()
        return mongoClient.getDatabase(databseConfig.database)
    }
}

package io.limberapp.backend.module.orgs.testing

import com.google.inject.Provides
import com.google.inject.Singleton
import com.mongodb.client.MongoDatabase
import io.ktor.application.Application
import io.limberapp.framework.MainModule
import io.limberapp.framework.config.Config
import io.limberapp.framework.createClient
import java.time.Clock

internal class TestMainModule(
    application: Application,
    clock: Clock,
    config: Config
) : MainModule(application, clock, config) {

    @Provides
    @Singleton
    fun mongoDatabase(config: Config): MongoDatabase {
        val mongoClient = config.createClient()
        return mongoClient.getDatabase(config.database.database)
    }
}

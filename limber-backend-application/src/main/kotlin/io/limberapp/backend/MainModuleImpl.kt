package io.limberapp.backend

import com.google.inject.Provides
import com.google.inject.Singleton
import com.mongodb.client.MongoDatabase
import io.ktor.application.Application
import io.limberapp.framework.MainModule
import io.limberapp.framework.config.Config
import io.limberapp.framework.createClient
import io.limberapp.framework.util.RandomUuidGenerator
import io.limberapp.framework.util.UuidGenerator
import java.time.Clock

internal class MainModuleImpl private constructor(
    application: Application,
    config: Config,
    clock: Clock,
    uuidGenerator: UuidGenerator
) : MainModule(application, clock, uuidGenerator, config) {

    @Provides
    @Singleton
    fun mongoDatabase(config: Config): MongoDatabase {
        val mongoClient = config.createClient()
        return mongoClient.getDatabase(config.database.database)
    }

    companion object {

        fun forProduction(application: Application, config: Config) = MainModuleImpl(
            application = application,
            config = config,
            clock = Clock.systemUTC(),
            uuidGenerator = RandomUuidGenerator()
        )
    }
}

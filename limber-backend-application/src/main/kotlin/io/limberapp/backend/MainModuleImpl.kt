package io.limberapp.backend

import com.google.inject.Provides
import com.google.inject.Singleton
import com.mongodb.client.MongoDatabase
import io.ktor.application.Application
import io.limberapp.framework.MainModule
import io.limberapp.framework.createClient
import io.limberapp.framework.util.uuidGenerator.RandomUuidGenerator
import io.limberapp.framework.util.uuidGenerator.UuidGenerator
import java.time.Clock

internal class MainModuleImpl private constructor(
    application: Application,
    config: Config,
    clock: Clock,
    uuidGenerator: UuidGenerator
) : MainModule(application, clock, config, uuidGenerator) {

    @Provides
    @Singleton
    fun mongoDatabase(config: Config): MongoDatabase {
        val mongoClient = config.database.createClient()
        return mongoClient.getDatabase(config.database.database)
    }

    companion object {

        fun forProduction(application: Application, config: Config) = MainModuleImpl(
            application = application,
            config = config,
            clock = Clock.systemUTC(), // For prod, use a real UTC clock.
            uuidGenerator = RandomUuidGenerator() // For prod, use a real/random UUID generator.
        )
    }
}

package com.piperframework.module

import com.google.inject.AbstractModule
import io.ktor.application.Application
import com.piperframework.config.Config
import com.piperframework.config.authentication.AuthenticationConfig
import com.piperframework.config.serving.ServingConfig
import com.piperframework.util.uuid.uuidGenerator.RandomUuidGenerator
import com.piperframework.util.uuid.uuidGenerator.UuidGenerator
import java.time.Clock

/**
 * MainModule configures bindings for classes that are not related to a specific application module.
 */
class MainModule(
    private val application: Application,
    private val clock: Clock,
    private val config: com.piperframework.config.Config,
    private val uuidGenerator: UuidGenerator
) : AbstractModule() {

    override fun configure() {
        bind(Application::class.java).toInstance(application)
        bind(Clock::class.java).toInstance(clock)
        bind(com.piperframework.config.authentication.AuthenticationConfig::class.java).toInstance(config.authentication)
        bind(com.piperframework.config.serving.ServingConfig::class.java).toInstance(config.serving)
        bind(UuidGenerator::class.java).toInstance(uuidGenerator)
    }

    companion object {

        fun forProduction(application: Application, config: com.piperframework.config.Config) = MainModule(
            application = application,
            config = config,
            clock = Clock.systemUTC(), // For prod, use a real UTC clock.
            uuidGenerator = RandomUuidGenerator() // For prod, use a real/random UUID generator.
        )
    }
}

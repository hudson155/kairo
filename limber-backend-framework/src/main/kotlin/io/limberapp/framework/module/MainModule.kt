package io.limberapp.framework.module

import com.google.inject.AbstractModule
import io.ktor.application.Application
import io.limberapp.framework.config.Config
import io.limberapp.framework.config.authentication.AuthenticationConfig
import io.limberapp.framework.config.serving.ServingConfig
import io.limberapp.framework.util.uuidGenerator.RandomUuidGenerator
import io.limberapp.framework.util.uuidGenerator.UuidGenerator
import java.time.Clock

/**
 * MainModule configures bindings for classes that are not related to a specific application module.
 */
class MainModule(
    private val application: Application,
    private val clock: Clock,
    private val config: Config,
    private val uuidGenerator: UuidGenerator
) : AbstractModule() {

    override fun configure() {
        bind(Application::class.java).toInstance(application)
        bind(Clock::class.java).toInstance(clock)
        bind(AuthenticationConfig::class.java).toInstance(config.authentication)
        bind(ServingConfig::class.java).toInstance(config.serving)
        bind(UuidGenerator::class.java).toInstance(uuidGenerator)
    }

    companion object {

        fun forProduction(application: Application, config: Config) = MainModule(
            application = application,
            config = config,
            clock = Clock.systemUTC(), // For prod, use a real UTC clock.
            uuidGenerator = RandomUuidGenerator() // For prod, use a real/random UUID generator.
        )
    }
}

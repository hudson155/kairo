package io.limberapp.framework

import com.google.inject.AbstractModule
import io.ktor.application.Application
import io.limberapp.framework.config.Config
import io.limberapp.framework.util.UuidGenerator
import java.time.Clock

/**
 * MainModule configures bindings for classes that are not related to a specific application module.
 */
abstract class MainModule(
    private val application: Application,
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator,
    private val config: Config
) : AbstractModule() {

    override fun configure() {
        bind(Application::class.java).toInstance(application)
        bind(Clock::class.java).toInstance(clock)
        bind(UuidGenerator::class.java).toInstance(uuidGenerator)
        bind(Config::class.java).toInstance(config)
    }
}

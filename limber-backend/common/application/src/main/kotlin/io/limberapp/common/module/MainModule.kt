package io.limberapp.common.module

import io.ktor.application.Application
import io.limberapp.common.config.Config
import io.limberapp.common.config.authentication.AuthenticationConfig
import io.limberapp.common.util.uuid.RandomUuidGenerator
import io.limberapp.common.util.uuid.UuidGenerator
import java.time.Clock

/**
 * MainModule configures bindings for classes that are not related to a specific application module.
 */
class MainModule(
  private val application: Application,
  private val clock: Clock,
  private val config: Config,
  private val uuidGenerator: UuidGenerator,
) : ModuleWithLifecycle() {
  override fun configure() {
    bind(Application::class.java).toInstance(application)
    bind(Clock::class.java).toInstance(clock)
    bind(AuthenticationConfig::class.java).toInstance(config.authentication)
    bind(UuidGenerator::class.java).toInstance(uuidGenerator)
  }

  override fun unconfigure() = Unit

  companion object {
    fun forProduction(application: Application, config: Config) = MainModule(
      application = application,
      config = config,
      clock = Clock.systemUTC(), // For prod, use a real UTC clock.
      uuidGenerator = RandomUuidGenerator() // For prod, use a real/random UUID generator.
    )
  }
}

package io.limberapp.module.main

import io.ktor.application.Application
import io.limberapp.common.module.GuiceModule
import io.limberapp.common.util.uuid.DeterministicUuidGenerator
import io.limberapp.common.util.uuid.RandomUuidGenerator
import io.limberapp.common.util.uuid.UuidGenerator
import io.limberapp.config.Config
import io.limberapp.config.authentication.AuthenticationConfig
import io.limberapp.config.authentication.ClockConfig
import io.limberapp.config.authentication.UuidsConfig
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

internal class MainModule(
  private val application: Application,
  private val config: Config,
) : GuiceModule() {
  override fun configure() {
    bind(Application::class.java).toInstance(application)
    bind(AuthenticationConfig::class.java).toInstance(config.authentication)
    bind(Clock::class.java).toInstance(config.clock.instantiate())
    bind(UuidGenerator::class.java).toInstance(config.uuids.instantiateGenerator())
  }

  private fun ClockConfig.instantiate() = when (type) {
    ClockConfig.Type.FIXED -> Clock.fixed(Instant.parse("2007-12-03T10:15:30.00Z"), ZoneId.of("America/New_York"))
    ClockConfig.Type.REAL -> Clock.systemUTC()
  }

  private fun UuidsConfig.instantiateGenerator() = when (generation) {
    UuidsConfig.Generation.DETERMINISTIC -> DeterministicUuidGenerator()
    UuidsConfig.Generation.RANDOM -> RandomUuidGenerator()
  }

  override fun unconfigure() = Unit
}

package io.limberapp.module.main

import io.ktor.application.Application
import io.limberapp.common.client.LimberHttpClient
import io.limberapp.common.client.LimberServerSelfAuthenticatingHttpClient
import io.limberapp.common.config.Config
import io.limberapp.common.config.authentication.AuthenticationConfig
import io.limberapp.common.config.authentication.AuthenticationMechanism
import io.limberapp.common.config.authentication.ClockConfig
import io.limberapp.common.config.authentication.UuidsConfig
import io.limberapp.common.module.GuiceModule
import io.limberapp.common.util.uuid.DeterministicUuidGenerator
import io.limberapp.common.util.uuid.RandomUuidGenerator
import io.limberapp.common.util.uuid.UuidGenerator
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
    bind(Clock::class.java).toInstance(clock())
    bind(UuidGenerator::class.java).toInstance(uuidGenerator())
    bind(LimberHttpClient::class.java).toInstance(httpClient())
  }

  private fun httpClient(): LimberHttpClient {
    val mechanism = config.authentication.mechanisms.filterIsInstance<AuthenticationMechanism.Jwt>().single()
    val algorithm = mechanism.createAlgorithm()
    return LimberServerSelfAuthenticatingHttpClient(config.monolithBaseUrl, algorithm, mechanism.issuer)
  }

  private fun clock() = when (config.clock.type) {
    ClockConfig.Type.FIXED -> Clock.fixed(Instant.parse("2007-12-03T10:15:30.00Z"), ZoneId.of("America/New_York"))
    ClockConfig.Type.REAL -> Clock.systemUTC()
  }

  private fun uuidGenerator() = when (config.uuids.generation) {
    UuidsConfig.Generation.DETERMINISTIC -> DeterministicUuidGenerator()
    UuidsConfig.Generation.RANDOM -> RandomUuidGenerator()
  }

  override fun unconfigure() = Unit
}

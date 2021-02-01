package io.limberapp.backend.module.main

import io.limberapp.common.config.ClockConfig
import io.limberapp.common.config.UuidsConfig
import io.limberapp.common.module.Module
import io.limberapp.common.serialization.LimberObjectMapper
import io.limberapp.common.server.Server
import io.limberapp.common.typeConversion.TypeConverter
import io.limberapp.common.util.uuid.DeterministicUuidGenerator
import io.limberapp.common.util.uuid.RandomUuidGenerator
import io.limberapp.common.util.uuid.UuidGenerator
import java.time.Clock
import java.time.ZoneId
import java.time.ZonedDateTime

internal class MainModule(
    private val clockConfig: ClockConfig,
    private val uuidsConfig: UuidsConfig,
    private val objectMapper: LimberObjectMapper,
) : Module() {
  /**
   * Don't add any type converters to the main module. They are ignored by [Server].
   */
  override val typeConverters: Set<TypeConverter<*>> = emptySet()

  override fun bind() {
    bind(Clock::class.java).toInstance(createClock())
    bind(LimberObjectMapper::class.java).toInstance(objectMapper)
    bind(UuidGenerator::class.java).toInstance(createUuidGenerator())
  }

  private fun createClock(): Clock = when (clockConfig.type) {
    ClockConfig.Type.FIXED -> run {
      // This arbitrary time zone and instant are used for fixed clock.
      val zoneId = ZoneId.of("America/New_York")
      val instant = ZonedDateTime.of(2007, 12, 3, 5, 15, 30, 789_000_000, zoneId).toInstant()
      return@run Clock.fixed(instant, zoneId)
    }
    ClockConfig.Type.REAL -> Clock.systemUTC() // Always use UTC if the clock is to be real.
  }

  private fun createUuidGenerator(): UuidGenerator = when (uuidsConfig.generation) {
    UuidsConfig.Generation.DETERMINISTIC -> DeterministicUuidGenerator()
    UuidsConfig.Generation.RANDOM -> RandomUuidGenerator()
  }

  /**
   * This method is not called by [Server] for the main module.
   */
  override fun cleanUp(): Unit = Unit
}

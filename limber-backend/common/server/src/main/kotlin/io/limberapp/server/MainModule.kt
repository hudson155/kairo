package io.limberapp.server

import com.google.inject.PrivateModule
import io.limberapp.config.ClockConfig
import io.limberapp.config.Config
import io.limberapp.config.UuidsConfig
import io.limberapp.module.typeLiteral
import io.limberapp.serialization.LimberObjectMapper
import io.limberapp.typeConversion.TypeConverter
import io.limberapp.util.uuid.DeterministicUuidGenerator
import io.limberapp.util.uuid.RandomUuidGenerator
import io.limberapp.util.uuid.UuidGenerator
import java.time.Clock
import java.time.ZoneId
import java.time.ZonedDateTime

internal class MainModule(
    private val config: Config,
    private val typeConverters: Set<TypeConverter<*>>,
) : PrivateModule() {
  override fun configure() {
    binder().requireAtInjectOnConstructors()

    with(typeLiteral<Set<TypeConverter<*>>>()) {
      bind(this).toInstance(typeConverters)
      expose(this)
    }

    with(LimberObjectMapper::class.java) {
      bind(this).toInstance(createObjectMapper())
      expose(this)
    }

    with(Clock::class.java) {
      bind(this).toInstance(createClock())
      expose(this)
    }

    with(UuidGenerator::class.java) {
      bind(this).toInstance(createUuidGenerator())
      expose(this)
    }
  }

  private fun createObjectMapper(): LimberObjectMapper =
      LimberObjectMapper(typeConverters = typeConverters)

  private fun createClock(): Clock = when (config.clock.type) {
    ClockConfig.Type.FIXED -> run {
      // This arbitrary time zone and instant are used for fixed clock.
      val zoneId = ZoneId.of("America/New_York")
      val instant = ZonedDateTime.of(2007, 12, 3, 5, 15, 30, 789_000_000, zoneId).toInstant()
      return@run Clock.fixed(instant, zoneId)
    }
    ClockConfig.Type.REAL -> Clock.systemUTC() // Always use UTC if the clock is to be real.
  }

  private fun createUuidGenerator(): UuidGenerator = when (config.uuids.generation) {
    UuidsConfig.Generation.DETERMINISTIC -> DeterministicUuidGenerator()
    UuidsConfig.Generation.RANDOM -> RandomUuidGenerator()
  }
}

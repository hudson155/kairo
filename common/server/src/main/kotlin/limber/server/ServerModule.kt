package limber.server

import com.google.inject.PrivateModule
import limber.config.ClockConfig
import limber.config.Config
import limber.config.IdsConfig
import limber.util.id.DeterministicGuidGenerator
import limber.util.id.DeterministicIdGenerator
import limber.util.id.GuidGenerator
import limber.util.id.IdGenerator
import limber.util.id.RandomGuidGenerator
import limber.util.id.RandomIdGenerator
import java.time.Clock
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.reflect.KClass

internal class ServerModule(private val config: Config) : PrivateModule() {
  override fun configure() {
    binder().requireAtInjectOnConstructors()

    bind(Clock::class.java).toInstance(createClock())
    expose(Clock::class.java)

    bind(GuidGenerator::class.java).to(guidGenerator().java).asEagerSingleton()
    expose(GuidGenerator::class.java)

    bind(IdGenerator.Factory::class.java).to(idGenerator().java).asEagerSingleton()
    expose(IdGenerator.Factory::class.java)
  }

  private fun createClock(): Clock =
    when (config.clock.type) {
      ClockConfig.Type.Fixed -> {
        // This arbitrary time zone and instant are used for fixed clock.
        val zoneId = ZoneId.of("America/Edmonton")
        val instant = ZonedDateTime.of(2007, 12, 3, 5, 15, 30, 789_000_000, zoneId).toInstant()
        Clock.fixed(instant, zoneId)
      }

      ClockConfig.Type.Real -> {
        // Always use UTC if the clock is to be real.
        Clock.systemUTC()
      }
    }

  private fun guidGenerator(): KClass<out GuidGenerator> =
    when (config.ids.generation) {
      IdsConfig.Generation.Deterministic -> DeterministicGuidGenerator::class
      IdsConfig.Generation.Random -> RandomGuidGenerator::class
    }

  private fun idGenerator(): KClass<out IdGenerator.Factory> =
    when (config.ids.generation) {
      IdsConfig.Generation.Deterministic -> DeterministicIdGenerator.Factory::class
      IdsConfig.Generation.Random -> RandomIdGenerator.Factory::class
    }
}

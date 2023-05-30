package limber.server

import com.google.inject.PrivateModule
import limber.config.ClockConfig
import limber.config.Config
import limber.config.GuidsConfig
import limber.util.id.DeterministicGuidGenerator
import limber.util.id.GuidGenerator
import limber.util.id.RandomGuidGenerator
import java.time.Clock
import java.time.ZoneId
import java.time.ZonedDateTime

internal class ServerModule(private val config: Config) : PrivateModule() {
  override fun configure() {
    binder().requireAtInjectOnConstructors()

    bind(Clock::class.java).toInstance(createClock())
    expose(Clock::class.java)

    bind(GuidGenerator::class.java).toInstance(createGuidGenerator())
    expose(GuidGenerator::class.java)
  }

  private fun createClock(): Clock =
    when (config.clock.type) {
      ClockConfig.Type.Fixed -> {
        // This arbitrary time zone and instant are used for fixed clock.
        val zoneId = ZoneId.of("America/Edmonton")
        val instant = ZonedDateTime.of(2007, 12, 3, 5, 15, 30, 789_000_000, zoneId).toInstant()
        Clock.fixed(instant, zoneId)
      }

      ClockConfig.Type.Real -> Clock.systemUTC() // Always use UTC if the clock is to be real.
    }

  private fun createGuidGenerator(): GuidGenerator =
    when (config.guids.generation) {
      GuidsConfig.Generation.Deterministic -> DeterministicGuidGenerator()
      GuidsConfig.Generation.Random -> RandomGuidGenerator()
    }
}

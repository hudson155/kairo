package kairo.clock

import java.time.Clock

internal object ClockFactory {
  fun create(config: KairoClockConfig): Clock =
    when (config) {
      is KairoClockConfig.Fixed -> Clock.fixed(config.instant, config.timeZone)
      is KairoClockConfig.System -> Clock.system(config.timeZone)
    }
}

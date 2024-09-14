package kairo.clock

import com.google.inject.Binder
import java.time.Clock
import kairo.dependencyInjection.bind
import kairo.feature.Feature
import kairo.feature.FeaturePriority

public open class KairoClockFeature(
  private val config: KairoClockConfig,
) : Feature() {
  override val name: String = "Kairo Clock"

  override val priority: FeaturePriority = FeaturePriority.Normal

  override fun bind(binder: Binder) {
    binder.bind<Clock>().toInstance(createClock(config))
  }

  private fun createClock(config: KairoClockConfig): Clock =
    when (config) {
      is KairoClockConfig.Fixed -> Clock.fixed(config.instant, config.timeZone)
      is KairoClockConfig.System -> Clock.system(config.timeZone)
    }
}

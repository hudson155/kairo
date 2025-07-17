package kairo.clock

import com.google.inject.Binder
import java.time.Clock
import kairo.dependencyInjection.bind
import kairo.feature.Feature
import kairo.feature.FeaturePriority

/**
 * The Clock Feature allows for configuration and injection of a Java clock.
 */
public open class KairoClockFeature(
  private val config: KairoClockConfig,
) : Feature() {
  final override val name: String = "Kairo Clock"

  final override val priority: FeaturePriority = FeaturePriority.Normal

  override fun bind(binder: Binder) {
    binder.bind<Clock>().toInstance(createClock(config))
  }

  private fun createClock(config: KairoClockConfig): Clock =
    ClockFactory.create(config)
}

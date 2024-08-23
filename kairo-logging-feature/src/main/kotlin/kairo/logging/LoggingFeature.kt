package kairo.logging

import com.google.inject.Injector
import kairo.feature.Feature
import kairo.feature.FeaturePriority
import org.apache.logging.log4j.LogManager

public class LoggingFeature(
  private val config: LoggingConfig,
) : Feature() {
  override val name: String = "Logging"

  override val priority: FeaturePriority = FeaturePriority.Monitoring

  override fun stop(injector: Injector?) {
    if (config.shutDownManually) {
      LogManager.shutdown()
    }
  }
}

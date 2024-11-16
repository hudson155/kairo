package kairo.googleCloudScheduler

import com.google.inject.Binder
import com.google.inject.Injector
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.dependencyInjection.bind
import kairo.dependencyInjection.getInstance
import kairo.dependencyInjection.toProvider

private val logger: KLogger = KotlinLogging.logger {}

public open class KairoGoogleCloudSchedulerFeature(
  private val config: KairoGoogleCloudSchedulerConfig,
) : BaseKairoGoogleCloudSchedulerFeature() {
  override fun bind(binder: Binder) {
    super.bind(binder)
    binder.bind<KairoGoogleCloudSchedulerConfig>().toInstance(config)
    binder.bind<GoogleCloudScheduler>().toProvider(GoogleCloudSchedulerProvider::class)
  }

  override fun stop(injector: Injector?) {
    injector ?: return
    logger.info { "Closing Google Cloud Scheduler." }
    val googleCloudScheduler = injector.getInstance<GoogleCloudScheduler>()
    googleCloudScheduler.close()
  }
}

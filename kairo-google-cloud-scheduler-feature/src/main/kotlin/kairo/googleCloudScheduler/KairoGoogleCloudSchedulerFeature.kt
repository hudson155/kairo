package kairo.googleCloudScheduler

import com.google.cloud.scheduler.v1.CloudSchedulerClient
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
    binder.bind<CloudSchedulerClient>().toProvider(CloudSchedulerClientProvider::class)
    binder.bind<GoogleCloudScheduler>().toProvider(GoogleCloudSchedulerProvider::class)
  }

  override fun stop(injector: Injector?) {
    injector ?: return
    logger.info { "Closing the GCP Cloud Scheduler client." }
    val cloudSchedulerClient = injector.getInstance<CloudSchedulerClient>()
    cloudSchedulerClient.close()
  }
}

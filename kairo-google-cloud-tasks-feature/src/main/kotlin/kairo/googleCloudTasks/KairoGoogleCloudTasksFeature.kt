package kairo.googleCloudTasks

import com.google.cloud.tasks.v2.CloudTasksClient
import com.google.inject.Binder
import com.google.inject.Injector
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.dependencyInjection.bind
import kairo.dependencyInjection.getInstance
import kairo.dependencyInjection.toProvider

private val logger: KLogger = KotlinLogging.logger {}

public open class KairoGoogleCloudTasksFeature(
  private val config: KairoGoogleCloudTasksConfig,
) : BaseKairoGoogleCloudTasksFeature() {
  override fun bind(binder: Binder) {
    super.bind(binder)
    binder.bind<KairoGoogleCloudTasksConfig>().toInstance(config)
    binder.bind<CloudTasksClient>().toProvider(CloudTasksClientProvider::class)
    binder.bind<GoogleCloudTasks>().toProvider(GoogleCloudTasksProvider::class)
  }

  override fun stop(injector: Injector?) {
    injector ?: return
    logger.info { "Closing the GCP Cloud Tasks client." }
    val cloudTasksClient = injector.getInstance<CloudTasksClient>()
    cloudTasksClient.close()
  }
}

package kairo.googleCloudTasks

import com.google.cloud.tasks.v2.CloudTasksClient
import com.google.inject.Binder
import com.google.inject.Injector
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.dependencyInjection.bind
import kairo.dependencyInjection.getInstance
import kairo.dependencyInjection.toProvider
import kairo.feature.Feature
import kairo.feature.FeaturePriority

private val logger: KLogger = KotlinLogging.logger {}

public open class KairoGoogleCloudTasksFeature(
  private val config: KairoGoogleCloudTasksConfig,
) : Feature() {
  override val name: String = "Google Cloud Tasks"

  override val priority: FeaturePriority = FeaturePriority.Framework

  override fun bind(binder: Binder) {
    binder.bind<KairoGoogleCloudTasksConfig>().toInstance(config)
    binder.bind<CloudTasksClient>().toProvider(CloudTasksClientProvider::class)
    binder.bind<TaskCreator>().toProvider(TaskCreatorProvider::class)
  }

  override fun stop(injector: Injector?) {
    injector ?: return
    logger.info { "Closing the GCP Cloud Tasks client." }
    val cloudTasksClient = injector.getInstance<CloudTasksClient>()
    cloudTasksClient.close()
  }
}

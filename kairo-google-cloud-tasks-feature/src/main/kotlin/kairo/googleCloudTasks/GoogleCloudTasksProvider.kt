package kairo.googleCloudTasks

import com.google.cloud.tasks.v2.CloudTasksClient
import com.google.inject.Inject
import com.google.inject.Singleton
import kairo.dependencyInjection.LazySingletonProvider

/**
 * There is a single global [GoogleCloudTasks] instance in Kairo.
 */
@Singleton
public class GoogleCloudTasksProvider @Inject constructor(
  private val cloudTasksClient: CloudTasksClient,
  private val config: KairoGoogleCloudTasksConfig,
) : LazySingletonProvider<GoogleCloudTasks>() {
  override fun create(): GoogleCloudTasks =
    create(config)

  private fun create(config: KairoGoogleCloudTasksConfig): GoogleCloudTasks =
    when (config) {
      is KairoGoogleCloudTasksConfig.Noop ->
        NoopGoogleCloudTasks()
      is KairoGoogleCloudTasksConfig.Real ->
        RealGoogleCloudTasks(
          cloudTasksClient = cloudTasksClient,
          tasksConfig = config,
        )
      is KairoGoogleCloudTasksConfig.TransactionAware ->
        TransactionAwareGoogleCloudTasks(
          delegate = create(config.delegate),
        )
    }
}

package kairo.googleCloudTasks

import com.google.cloud.tasks.v2.CloudTasksClient
import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import kairo.dependencyInjection.LazySingletonProvider
import kairo.rest.client.RestClient

/**
 * There is a single global [GoogleCloudTasks] instance in Kairo.
 */
@Singleton
public class GoogleCloudTasksProvider @Inject constructor(
  private val config: KairoGoogleCloudTasksConfig,
  private val restClient: Provider<RestClient>,
) : LazySingletonProvider<GoogleCloudTasks>() {
  override fun create(): GoogleCloudTasks =
    create(config)

  private fun create(config: KairoGoogleCloudTasksConfig): GoogleCloudTasks =
    when (config) {
      is KairoGoogleCloudTasksConfig.Http ->
        HttpGoogleCloudTasks(
          cloudTasksClient = CloudTasksClient.create(),
          tasksConfig = config,
        )
      is KairoGoogleCloudTasksConfig.Local ->
        LocalGoogleCloudTasks(restClient)
      is KairoGoogleCloudTasksConfig.Noop ->
        NoopGoogleCloudTasks()
      is KairoGoogleCloudTasksConfig.TransactionAware ->
        TransactionAwareGoogleCloudTasks(
          delegate = create(config.delegate),
        )
    }
}

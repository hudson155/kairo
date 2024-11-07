package kairo.googleCloudTasks

import com.google.cloud.tasks.v2.CloudTasksClient
import com.google.inject.Inject
import com.google.inject.Singleton
import kairo.dependencyInjection.LazySingletonProvider

/**
 * There is a single global [TaskCreator] instance in Kairo.
 */
@Singleton
public class TaskCreatorProvider @Inject constructor(
  private val cloudTasksClient: CloudTasksClient,
  private val config: KairoGoogleCloudTasksConfig,
) : LazySingletonProvider<TaskCreator>() {
  override fun create(): TaskCreator =
    create(config)

  private fun create(config: KairoGoogleCloudTasksConfig): TaskCreator =
    when (config) {
      is KairoGoogleCloudTasksConfig.Noop ->
        NoopTaskCreator()
      is KairoGoogleCloudTasksConfig.Real ->
        RealTaskCreator(
          cloudTasksClient = cloudTasksClient,
          tasksConfig = config,
        )
      is KairoGoogleCloudTasksConfig.TransactionAware ->
        TransactionAwareTaskCreator(
          delegate = create(config.delegate),
        )
    }
}

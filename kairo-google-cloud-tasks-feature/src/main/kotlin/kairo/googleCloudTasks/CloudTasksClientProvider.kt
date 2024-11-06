package kairo.googleCloudTasks

import com.google.cloud.tasks.v2.CloudTasksClient
import com.google.inject.Inject
import com.google.inject.Singleton
import kairo.dependencyInjection.LazySingletonProvider

/**
 * There is a single global [CloudTasksClient] instance in Kairo.
 */
@Singleton
public class CloudTasksClientProvider @Inject constructor() : LazySingletonProvider<CloudTasksClient>() {
  override fun create(): CloudTasksClient =
    CloudTasksClient.create()
}

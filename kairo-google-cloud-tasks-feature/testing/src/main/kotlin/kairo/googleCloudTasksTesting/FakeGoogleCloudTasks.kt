package kairo.googleCloudTasksTesting

import com.google.inject.Injector
import kairo.dependencyInjection.getInstance
import kairo.googleCloudTasks.GoogleCloudTasks
import kairo.rest.endpoint.RestEndpoint

public class FakeGoogleCloudTasks : GoogleCloudTasks() {
  internal val createdTasks: MutableList<Task> = mutableListOf()

  override suspend fun create(endpoint: RestEndpoint<*, *>) {
    val task = task(endpoint)
    createdTasks += task
  }

  override fun close(): Unit = Unit

  public fun reset() {
    createdTasks.clear()
  }
}

public fun getCreatedTasks(
  injector: Injector,
): MutableList<GoogleCloudTasks.Task> {
  val googleCloudTasks = injector.getInstance<GoogleCloudTasks>() as FakeGoogleCloudTasks
  return googleCloudTasks.createdTasks
}

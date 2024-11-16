package kairo.googleCloudTasksTesting

import com.google.inject.Injector
import kairo.dependencyInjection.getInstance
import kairo.googleCloudTasks.GoogleCloudTasks

public class FakeGoogleCloudTasks : GoogleCloudTasks() {
  internal val createdTasks: MutableList<Task> = mutableListOf()

  override suspend fun create(task: Task) {
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

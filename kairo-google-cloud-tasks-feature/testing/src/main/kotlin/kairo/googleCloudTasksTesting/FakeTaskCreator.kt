package kairo.googleCloudTasksTesting

import com.google.inject.Injector
import kairo.dependencyInjection.getInstance
import kairo.googleCloudTasks.TaskCreator
import kairo.rest.endpoint.RestEndpoint

public class FakeTaskCreator : TaskCreator() {
  internal val createdTasks: MutableList<RestEndpoint<*, *>> = mutableListOf()

  public override suspend fun create(endpoint: RestEndpoint<*, *>) {
    createdTasks += endpoint
  }

  public fun reset() {
    createdTasks.clear()
  }
}

public fun getCreatedTasks(injector: Injector): List<RestEndpoint<*, *>> {
  val taskCreator = injector.getInstance<TaskCreator>() as FakeTaskCreator
  return taskCreator.createdTasks
}

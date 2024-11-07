package kairo.googleCloudTasksTesting

import com.google.inject.Injector
import kairo.dependencyInjection.getInstance
import kairo.googleCloudTasks.TaskCreator

public class FakeTaskCreator : TaskCreator() {
  internal val createdTasks: MutableList<Task> = mutableListOf()

  override suspend fun create(task: Task) {
    createdTasks += task
  }

  public fun reset() {
    createdTasks.clear()
  }
}

public fun getCreatedTasks(injector: Injector): List<TaskCreator.Task> {
  val taskCreator = injector.getInstance<TaskCreator>() as FakeTaskCreator
  return taskCreator.createdTasks
}

package kairo.googleCloudTasks

public class NoopTaskCreator : TaskCreator() {
  override suspend fun create(task: Task): Unit = Unit
}

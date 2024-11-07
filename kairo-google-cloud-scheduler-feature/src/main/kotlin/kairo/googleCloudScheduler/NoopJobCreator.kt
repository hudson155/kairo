package kairo.googleCloudScheduler

public class NoopJobCreator : JobCreator() {
  override suspend fun create(job: Job, config: Config): Unit = Unit
}

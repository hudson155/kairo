package kairo.googleCloudScheduler

public class NoopGoogleCloudScheduler : GoogleCloudScheduler() {
  override suspend fun create(job: Job, config: Config): Unit = Unit

  override suspend fun delete(name: String): Unit = Unit

  override fun close(): Unit = Unit
}

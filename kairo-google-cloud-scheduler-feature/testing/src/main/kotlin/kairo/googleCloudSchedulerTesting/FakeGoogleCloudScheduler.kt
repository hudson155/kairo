package kairo.googleCloudSchedulerTesting

import com.google.inject.Injector
import kairo.dependencyInjection.getInstance
import kairo.googleCloudScheduler.GoogleCloudScheduler

public class FakeGoogleCloudScheduler : GoogleCloudScheduler() {
  internal val createdJobs: MutableList<Pair<Job, Config>> = mutableListOf()

  internal val deletedJobs: MutableList<String> = mutableListOf()

  override suspend fun create(job: Job, config: Config) {
    createdJobs += Pair(job, config)
  }

  override suspend fun delete(name: String) {
    deletedJobs += name
  }

  public fun reset() {
    createdJobs.clear()
    deletedJobs.clear()
  }
}

public fun getCreatedJobs(
  injector: Injector,
): MutableList<Pair<GoogleCloudScheduler.Job, GoogleCloudScheduler.Config>> {
  val googleCloudScheduler = injector.getInstance<GoogleCloudScheduler>() as FakeGoogleCloudScheduler
  return googleCloudScheduler.createdJobs
}

public fun getDeletedJobs(
  injector: Injector,
): MutableList<String> {
  val googleCloudScheduler = injector.getInstance<GoogleCloudScheduler>() as FakeGoogleCloudScheduler
  return googleCloudScheduler.deletedJobs
}

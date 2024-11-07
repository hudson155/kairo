package kairo.googleCloudSchedulerTesting

import com.google.inject.Injector
import kairo.dependencyInjection.getInstance
import kairo.googleCloudScheduler.JobCreator

public class FakeJobCreator : JobCreator() {
  internal val createdJobs: MutableList<Pair<Job, Config>> = mutableListOf()

  override suspend fun create(job: Job, config: Config) {
    createdJobs += Pair(job, config)
  }

  public fun reset() {
    createdJobs.clear()
  }
}

public fun getCreatedJobs(injector: Injector): List<Pair<JobCreator.Job, JobCreator.Config>> {
  val jobCreator = injector.getInstance<JobCreator>() as FakeJobCreator
  return jobCreator.createdJobs
}

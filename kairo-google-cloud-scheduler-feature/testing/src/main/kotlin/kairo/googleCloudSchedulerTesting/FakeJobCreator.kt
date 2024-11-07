package kairo.googleCloudSchedulerTesting

import com.google.inject.Injector
import kairo.dependencyInjection.getInstance
import kairo.googleCloudScheduler.JobCreator
import kairo.rest.endpoint.RestEndpoint

public class FakeJobCreator : JobCreator() {
  internal val createdJobs: MutableList<Pair<RestEndpoint<*, *>, Config>> = mutableListOf()

  public override suspend fun create(endpoint: RestEndpoint<*, *>, config: Config) {
    createdJobs += Pair(endpoint, config)
  }

  public fun reset() {
    createdJobs.clear()
  }
}

public fun getCreatedJobs(injector: Injector): List<Pair<RestEndpoint<*, *>, JobCreator.Config>> {
  val jobCreator = injector.getInstance<JobCreator>() as FakeJobCreator
  return jobCreator.createdJobs
}

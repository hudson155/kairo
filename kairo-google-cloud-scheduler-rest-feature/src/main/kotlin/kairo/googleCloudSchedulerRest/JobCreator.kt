package kairo.googleCloudSchedulerRest

import kairo.googleCloudScheduler.GoogleCloudScheduler
import kairo.googleCloudTasks.GoogleCloudTasks
import kairo.rest.endpoint.RestEndpoint

public suspend fun GoogleCloudScheduler.createTask(endpoint: RestEndpoint<*, *>, config: GoogleCloudScheduler.Config) {
  val task = GoogleCloudTasks.Task.from(endpoint)
  val job = GoogleCloudScheduler.Job.from(GoogleCloudSchedulerApi.CreateTask(task))
  create(job, config)
}

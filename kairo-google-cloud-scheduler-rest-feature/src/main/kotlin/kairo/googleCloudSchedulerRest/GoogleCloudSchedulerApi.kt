package kairo.googleCloudSchedulerRest

import kairo.googleCloudTasks.GoogleCloudTasks
import kairo.rest.endpoint.RestEndpoint

public object GoogleCloudSchedulerApi {
  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/jobs")
  @RestEndpoint.ContentType("application/json")
  @RestEndpoint.Accept("application/json")
  public data class CreateTask(
    override val body: GoogleCloudTasks.Task,
  ) : RestEndpoint<GoogleCloudTasks.Task, Unit>()
}

package kairo.googleCloudTasks

import kairo.rest.endpoint.RestEndpoint

public class NoopTaskCreator : TaskCreator() {
  public override suspend fun create(endpoint: RestEndpoint<*, *>): Unit = Unit
}

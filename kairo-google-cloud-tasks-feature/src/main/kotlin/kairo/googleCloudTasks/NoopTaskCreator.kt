package kairo.googleCloudTasks

import kairo.rest.endpoint.RestEndpoint

public class NoopTaskCreator : TaskCreator() {
  public override suspend fun <E : RestEndpoint<I, *>, I : Any> create(endpoint: E): Unit = Unit
}

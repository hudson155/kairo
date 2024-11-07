package kairo.googleCloudScheduler

import kairo.rest.endpoint.RestEndpoint

public class NoopJobCreator : JobCreator() {
  public override suspend fun create(endpoint: RestEndpoint<*, *>, config: Config): Unit = Unit
}

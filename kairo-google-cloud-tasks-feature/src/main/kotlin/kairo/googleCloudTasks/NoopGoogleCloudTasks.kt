package kairo.googleCloudTasks

import kairo.rest.endpoint.RestEndpoint

public class NoopGoogleCloudTasks : GoogleCloudTasks() {
  override suspend fun create(endpoint: RestEndpoint<*, *>): Unit = Unit

  override fun close(): Unit = Unit
}

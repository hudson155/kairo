package kairo.googleCloudScheduler

import kairo.rest.endpoint.RestEndpoint
import kairo.rest.endpoint.RestEndpointDetails
import kairo.rest.writer.RestEndpointWriter

/**
 * Primarily, [RealGoogleCloudScheduler].
 */
public abstract class GoogleCloudScheduler {
  public data class Job(
    val details: RestEndpointDetails,
  ) {
    public companion object {
      public fun from(endpoint: RestEndpoint<*, *>): Job =
        Job(
          details = RestEndpointWriter.from(endpoint::class).write(endpoint),
        )
    }
  }

  public data class Config(
    val name: String,
    val description: String,
    val schedule: String,
  )

  public suspend fun create(endpoint: RestEndpoint<*, *>, config: Config) {
    val job = Job.from(endpoint)
    return create(job, config)
  }

  public abstract suspend fun create(job: Job, config: Config)

  public abstract suspend fun delete(name: String)

  public abstract fun close()
}

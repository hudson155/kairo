package kairo.googleCloudScheduler

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.rest.endpoint.RestEndpoint
import kairo.rest.endpoint.RestEndpointDetails
import kairo.rest.writer.RestEndpointWriter

private val logger: KLogger = KotlinLogging.logger {}

/**
 * Primarily, [RealJobCreator].
 */
public abstract class JobCreator {
  public data class Job(
    val details: RestEndpointDetails,
  )

  public data class Config(
    val name: String,
    val description: String,
    val schedule: String,
  )

  public suspend fun create(endpoint: RestEndpoint<*, *>, config: Config) {
    logger.info { "Creating job for endpoint: $endpoint (config $config)." }
    val job = Job(
      details = RestEndpointWriter.from(endpoint::class).write(endpoint),
    )
    return create(job, config)
  }

  public abstract suspend fun create(job: Job, config: Config)
}

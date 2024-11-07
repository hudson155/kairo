package kairo.googleCloudScheduler

import kairo.rest.endpoint.RestEndpoint

/**
 * Primarily, [RealJobCreator].
 */
public abstract class JobCreator {
  @Target(AnnotationTarget.CLASS)
  public annotation class Queue(val name: String)

  public data class Config(
    val name: String,
    val description: String,
    val schedule: String,
  )

  public abstract suspend fun create(endpoint: RestEndpoint<*, *>, config: Config)
}

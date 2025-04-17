package kairo.googleCloudTasks

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.rest.endpoint.RestEndpoint
import kairo.rest.endpoint.RestEndpointDetails
import kairo.rest.writer.RestEndpointWriter
import kotlin.reflect.full.findAnnotation

private val logger: KLogger = KotlinLogging.logger {}

/**
 * Primarily, [RealGoogleCloudTasks].
 */
public abstract class GoogleCloudTasks {
  @Target(AnnotationTarget.CLASS)
  public annotation class Queue(val name: String)

  public data class Task(
    val queueName: String,
    val details: RestEndpointDetails,
  )

  public abstract suspend fun create(endpoint: RestEndpoint<*, *>)

  protected fun task(endpoint: RestEndpoint<*, *>): Task =
    Task(
      queueName = queueName(endpoint),
      details = RestEndpointWriter.from(endpoint::class).write(endpoint),
    )

  private fun queueName(endpoint: RestEndpoint<*, *>): String {
    val endpointKClass = endpoint::class
    val annotation = endpointKClass.findAnnotation<Queue>()
    requireNotNull(annotation) {
      "REST endpoint ${endpointKClass.qualifiedName!!} is missing @${Queue::class.simpleName!!}."
    }
    return annotation.name
  }

  public abstract fun close()
}

package kairo.googleCloudTasks

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.rest.endpoint.RestEndpoint
import kairo.rest.endpoint.RestEndpointDetails
import kairo.rest.writer.RestEndpointWriter
import kotlin.reflect.full.findAnnotation

private val logger: KLogger = KotlinLogging.logger {}

/**
 * Primarily, [RealTaskCreator].
 */
public abstract class TaskCreator {
  @Target(AnnotationTarget.CLASS)
  public annotation class Queue(val name: String)

  public data class Task(
    val queueName: String,
    val details: RestEndpointDetails,
  )

  public suspend fun create(endpoint: RestEndpoint<*, *>) {
    logger.info { "Creating task for endpoint: $endpoint." }
    val task = Task(
      queueName = queueName(endpoint),
      details = RestEndpointWriter.from(endpoint::class).write(endpoint),
    )
    return create(task)
  }

  public abstract suspend fun create(task: Task)

  private fun queueName(endpoint: RestEndpoint<*, *>): String {
    val endpointKClass = endpoint::class
    val annotation = endpointKClass.findAnnotation<Queue>()
    requireNotNull(annotation) {
      "REST endpoint ${endpointKClass.qualifiedName!!} is missing @${Queue::class.simpleName!!}."
    }
    return annotation.name
  }
}

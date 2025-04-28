package kairo.googleCloudTasks

import com.google.cloud.tasks.v2.CloudTasksClient
import com.google.cloud.tasks.v2.CreateTaskRequest
import com.google.cloud.tasks.v2.QueueName
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.googleCommon.await
import kairo.rest.endpoint.RestEndpoint
import kairo.rest.endpoint.RestEndpointDetails

private val logger: KLogger = KotlinLogging.logger {}

public abstract class RealGoogleCloudTasks(
  private val cloudTasksClient: CloudTasksClient,
  private val tasksConfig: KairoGoogleCloudTasksConfig.Real,
) : GoogleCloudTasks() {
  final override suspend fun create(endpoint: RestEndpoint<*, *>) {
    logger.info { "Creating task for endpoint: $endpoint." }
    val task = task(endpoint)
    logger.info { "Creating task: $task." }
    val request = CreateTaskRequest.newBuilder().apply {
      this.parent = buildQueueName(task.queueName).toString()
      this.task = buildTask(task.details)
    }.build()
    cloudTasksClient.createTaskCallable().futureCall(request).await()
  }

  final override fun close() {
    cloudTasksClient.close()
  }

  private fun buildQueueName(queueName: String): QueueName {
    val queue = listOfNotNull(
      tasksConfig.queueName.prefix,
      queueName,
      tasksConfig.queueName.suffix,
    ).joinToString("")
    return QueueName.of(tasksConfig.projectId, tasksConfig.location, queue)
  }

  protected abstract fun buildTask(details: RestEndpointDetails): com.google.cloud.tasks.v2.Task
}

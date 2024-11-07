package kairo.googleCloudTasks

import com.google.cloud.tasks.v2.AppEngineHttpRequest
import com.google.cloud.tasks.v2.AppEngineRouting
import com.google.cloud.tasks.v2.CloudTasksClient
import com.google.cloud.tasks.v2.CreateTaskRequest
import com.google.cloud.tasks.v2.HttpMethod
import com.google.cloud.tasks.v2.QueueName
import com.google.protobuf.ByteString
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpHeaders
import kairo.googleCommon.await
import kairo.rest.endpoint.RestEndpointDetails
import kairo.rest.ktorServerMapper

private val logger: KLogger = KotlinLogging.logger {}

public class RealGoogleCloudTasks(
  private val cloudTasksClient: CloudTasksClient,
  private val tasksConfig: KairoGoogleCloudTasksConfig.Real,
) : GoogleCloudTasks() {
  public override suspend fun create(task: Task) {
    logger.info { "Creating task: $task." }
    val request = CreateTaskRequest.newBuilder().apply {
      parent = buildQueueName(task.queueName).toString()
      this.task = buildTask(task.details)
    }.build()
    cloudTasksClient.createTaskCallable().futureCall(request).await()
  }

  private fun buildQueueName(queueName: String): QueueName {
    val queue = listOfNotNull(
      tasksConfig.queueName.prefix,
      queueName,
      tasksConfig.queueName.suffix,
    ).joinToString("")
    return QueueName.of(tasksConfig.projectId, tasksConfig.location, queue)
  }

  private fun buildTask(details: RestEndpointDetails): com.google.cloud.tasks.v2.Task =
    com.google.cloud.tasks.v2.Task.newBuilder().apply {
      @Suppress("DuplicatedCode")
      appEngineHttpRequest = AppEngineHttpRequest.newBuilder().apply {
        appEngineRouting = buildAppEngineRouting()
        httpMethod = HttpMethod.valueOf(details.method.value)
        relativeUri = details.path
        details.contentType?.let { putHeaders(HttpHeaders.ContentType, it.toString()) }
        details.accept?.let { putHeaders(HttpHeaders.Accept, it.toString()) }
        details.body?.let { body = ByteString.copyFrom(ktorServerMapper.writeValueAsBytes(it)) }
      }.build()
    }.build()

  private fun buildAppEngineRouting(): AppEngineRouting =
    AppEngineRouting.newBuilder().apply {
      service = tasksConfig.service
    }.build()
}

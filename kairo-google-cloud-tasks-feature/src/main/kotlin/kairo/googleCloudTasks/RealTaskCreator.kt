package kairo.googleCloudTasks

import com.google.cloud.tasks.v2.AppEngineHttpRequest
import com.google.cloud.tasks.v2.AppEngineRouting
import com.google.cloud.tasks.v2.CloudTasksClient
import com.google.cloud.tasks.v2.CreateTaskRequest
import com.google.cloud.tasks.v2.HttpMethod
import com.google.cloud.tasks.v2.QueueName
import com.google.cloud.tasks.v2.Task
import com.google.protobuf.ByteString
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpHeaders
import kairo.googleCommon.await
import kairo.rest.endpoint.RestEndpoint
import kairo.rest.ktorServerMapper
import kairo.rest.writer.RestEndpointWriter
import kotlin.reflect.full.findAnnotation

private val logger: KLogger = KotlinLogging.logger {}

public class RealTaskCreator(
  private val cloudTasksClient: CloudTasksClient,
  private val tasksConfig: KairoGoogleCloudTasksConfig.Real,
) : TaskCreator() {
  public override suspend fun create(endpoint: RestEndpoint<*, *>) {
    logger.info { "Creating task: $endpoint." }
    val request = CreateTaskRequest.newBuilder().apply {
      parent = buildQueueName(endpoint).toString()
      task = buildTask(endpoint)
    }.build()
    cloudTasksClient.createTaskCallable().futureCall(request).await()
  }

  private fun buildQueueName(endpoint: RestEndpoint<*, *>): QueueName {
    val endpointKClass = endpoint::class
    val annotation = endpointKClass.findAnnotation<Queue>()
    requireNotNull(annotation) {
      "REST endpoint ${endpointKClass.qualifiedName!!} is missing @${Queue::class.simpleName!!}."
    }
    val queue = listOfNotNull(
      tasksConfig.queueName.prefix,
      annotation.name,
      tasksConfig.queueName.suffix,
    ).joinToString("")
    return QueueName.of(tasksConfig.projectId, tasksConfig.location, queue)
  }

  private fun buildTask(endpoint: RestEndpoint<*, *>): Task {
    val details = RestEndpointWriter.from(endpoint::class).write(endpoint)
    return Task.newBuilder().apply {
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
  }

  private fun buildAppEngineRouting(): AppEngineRouting =
    AppEngineRouting.newBuilder().apply {
      service = tasksConfig.service
    }.build()
}

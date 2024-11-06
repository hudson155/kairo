package kairo.googleCloudTasks

import com.google.cloud.tasks.v2.AppEngineHttpRequest
import com.google.cloud.tasks.v2.CloudTasksClient
import com.google.cloud.tasks.v2.CreateTaskRequest
import com.google.cloud.tasks.v2.HttpMethod
import com.google.cloud.tasks.v2.QueueName
import com.google.cloud.tasks.v2.Task
import com.google.protobuf.ByteString
import io.ktor.http.HttpHeaders
import kairo.googleCommon.await
import kairo.rest.endpoint.RestEndpoint
import kairo.rest.ktorServerMapper
import kairo.rest.writer.RestEndpointWriter
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

public class RealTaskCreator(
  private val cloudTasksClient: CloudTasksClient,
  private val config: KairoGoogleCloudTasksConfig.Real,
) : TaskCreator() {
  public override suspend fun create(endpoint: RestEndpoint<*, *>) {
    val queueName = deriveQueueName(endpoint::class)
    val request = CreateTaskRequest.newBuilder().apply {
      parent = buildParent(queueName)
      task = buildTask(endpoint)
    }.build()
    cloudTasksClient.createTaskCallable().futureCall(request).await()
  }

  private fun <E : RestEndpoint<*, *>> deriveQueueName(endpoint: KClass<E>): String {
    val annotation = endpoint.findAnnotation<Queue>()
    requireNotNull(annotation) {
      "REST endpoint ${endpoint.qualifiedName!!} is missing @${Queue::class.simpleName!!}."
    }
    return listOfNotNull(
      config.queueName.prefix,
      annotation.name,
      config.queueName.suffix,
    ).joinToString("")
  }

  private fun buildParent(queueName: String): String =
    QueueName.of(config.projectId, config.location, queueName).toString()

  private fun buildTask(endpoint: RestEndpoint<*, *>): Task? {
    val details = RestEndpointWriter.from(endpoint::class).write(endpoint)
    return Task.newBuilder().apply {
      appEngineHttpRequest = AppEngineHttpRequest.newBuilder().apply {
        httpMethod = HttpMethod.valueOf(details.method.value)
        relativeUri = details.path
        details.contentType?.let { putHeaders(HttpHeaders.ContentType, it.toString()) }
        details.accept?.let { putHeaders(HttpHeaders.Accept, it.toString()) }
        details.body?.let { body = ByteString.copyFrom(ktorServerMapper.writeValueAsBytes(it)) }
      }.build()
    }.build()
  }
}

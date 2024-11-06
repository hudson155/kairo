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
  public override suspend fun <E : RestEndpoint<I, *>, I : Any> create(endpoint: E) {
    val details = RestEndpointWriter.from(endpoint::class).write(endpoint)
    val queueName = deriveQueueName(endpoint::class)
    val parent = QueueName.of(config.projectId, config.location, queueName).toString();
    val task = Task.newBuilder().apply {
      appEngineHttpRequest = AppEngineHttpRequest.newBuilder().apply {
        httpMethod = HttpMethod.valueOf(details.method.value)
        relativeUri = details.path
        details.contentType?.let { contentType ->
          putHeaders(HttpHeaders.ContentType, contentType.toString())
        }
        details.accept?.let { accept ->
          putHeaders(HttpHeaders.Accept, accept.toString())
        }
        details.body?.let { body ->
          this.body = ByteString.copyFrom(ktorServerMapper.writeValueAsBytes(body))
        }
      }.build()
    }.build()
    val request = CreateTaskRequest.newBuilder().apply {
      this.parent = parent
      this.task = task
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
}

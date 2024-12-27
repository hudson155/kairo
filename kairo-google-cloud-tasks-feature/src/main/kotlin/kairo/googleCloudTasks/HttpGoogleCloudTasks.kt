package kairo.googleCloudTasks

import com.google.cloud.tasks.v2.CloudTasksClient
import com.google.cloud.tasks.v2.HttpMethod
import com.google.cloud.tasks.v2.HttpRequest
import com.google.protobuf.ByteString
import io.ktor.http.HttpHeaders
import io.ktor.http.URLBuilder
import io.ktor.http.takeFrom
import kairo.rest.KtorServerMapper
import kairo.rest.endpoint.RestEndpointDetails

public class HttpGoogleCloudTasks(
  cloudTasksClient: CloudTasksClient,
  private val tasksConfig: KairoGoogleCloudTasksConfig.Http,
) : RealGoogleCloudTasks(
  cloudTasksClient = cloudTasksClient,
  tasksConfig = tasksConfig,
) {
  override fun buildTask(details: RestEndpointDetails): com.google.cloud.tasks.v2.Task =
    com.google.cloud.tasks.v2.Task.newBuilder().apply {
      @Suppress("DuplicatedCode")
      httpRequest = HttpRequest.newBuilder().apply {
        url = buildUrl(details)
        httpMethod = HttpMethod.valueOf(details.method.value)
        details.contentType?.let { putHeaders(HttpHeaders.ContentType, it.toString()) }
        details.accept?.let { putHeaders(HttpHeaders.Accept, it.toString()) }
        details.body?.let { body = ByteString.copyFrom(KtorServerMapper.json.writeValueAsBytes(it)) }
      }.build()
    }.build()

  private fun buildUrl(details: RestEndpointDetails): String =
    URLBuilder().takeFrom(tasksConfig.baseUrl).takeFrom(details.path).buildString()
}

package kairo.googleCloudTasks

import com.google.cloud.tasks.v2.AppEngineHttpRequest
import com.google.cloud.tasks.v2.AppEngineRouting
import com.google.cloud.tasks.v2.CloudTasksClient
import com.google.cloud.tasks.v2.HttpMethod
import com.google.protobuf.ByteString
import io.ktor.http.HttpHeaders
import kairo.rest.KtorServerMapper
import kairo.rest.endpoint.RestEndpointDetails

public class GoogleAppEngineGoogleCloudTasks(
  cloudTasksClient: CloudTasksClient,
  private val tasksConfig: KairoGoogleCloudTasksConfig.GoogleAppEngine,
) : RealGoogleCloudTasks(
  cloudTasksClient = cloudTasksClient,
  tasksConfig = tasksConfig,
) {
  override fun buildTask(details: RestEndpointDetails): com.google.cloud.tasks.v2.Task =
    com.google.cloud.tasks.v2.Task.newBuilder().apply {
      @Suppress("DuplicatedCode")
      appEngineHttpRequest = AppEngineHttpRequest.newBuilder().apply {
        appEngineRouting = buildAppEngineRouting()
        httpMethod = HttpMethod.valueOf(details.method.value)
        relativeUri = details.path
        details.contentType?.let { putHeaders(HttpHeaders.ContentType, it.toString()) }
        details.accept?.let { putHeaders(HttpHeaders.Accept, it.toString()) }
        details.body?.let { body = ByteString.copyFrom(KtorServerMapper.json.writeValueAsBytes(it)) }
      }.build()
    }.build()

  private fun buildAppEngineRouting(): AppEngineRouting =
    AppEngineRouting.newBuilder().apply {
      service = tasksConfig.service
    }.build()
}

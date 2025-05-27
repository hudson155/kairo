package kairo.googleCloudScheduler

import com.google.cloud.scheduler.v1.AppEngineHttpTarget
import com.google.cloud.scheduler.v1.AppEngineRouting
import com.google.cloud.scheduler.v1.CloudSchedulerClient
import com.google.cloud.scheduler.v1.HttpMethod
import com.google.protobuf.ByteString
import io.ktor.http.HttpHeaders
import kairo.rest.KtorServerMapper
import kairo.rest.endpoint.RestEndpointDetails

public class GoogleAppEngineGoogleCloudScheduler(
  cloudSchedulerClient: CloudSchedulerClient,
  private val schedulerConfig: KairoGoogleCloudSchedulerConfig.GoogleAppEngine,
) : RealGoogleCloudScheduler(
  cloudSchedulerClient = cloudSchedulerClient,
  schedulerConfig = schedulerConfig,
) {
  override fun buildJob(details: RestEndpointDetails, config: Config): com.google.cloud.scheduler.v1.Job =
    com.google.cloud.scheduler.v1.Job.newBuilder().apply {
      name = buildJobName(config.name).toString()
      description = buildJobDescription(config)
      @Suppress("DuplicatedCode")
      appEngineHttpTarget = AppEngineHttpTarget.newBuilder().apply {
        appEngineRouting = buildAppEngineRouting()
        httpMethod = HttpMethod.valueOf(details.method.value)
        relativeUri = details.path
        details.contentType?.let { putHeaders(HttpHeaders.ContentType, it.toString()) }
        details.accept?.let { putHeaders(HttpHeaders.Accept, it.toString()) }
        details.body?.let { body = ByteString.copyFrom(KtorServerMapper.json.writeValueAsBytes(it)) }
      }.build()
      schedule = config.schedule
    }.build()

  private fun buildAppEngineRouting(): AppEngineRouting =
    AppEngineRouting.newBuilder().apply {
      service = schedulerConfig.service
    }.build()
}

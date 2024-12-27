package kairo.googleCloudScheduler

import com.google.cloud.scheduler.v1.CloudSchedulerClient
import com.google.cloud.scheduler.v1.HttpMethod
import com.google.cloud.scheduler.v1.HttpTarget
import com.google.protobuf.ByteString
import io.ktor.http.HttpHeaders
import io.ktor.http.URLBuilder
import io.ktor.http.takeFrom
import kairo.rest.KtorServerMapper
import kairo.rest.endpoint.RestEndpointDetails

public class HttpGoogleCloudScheduler(
  cloudSchedulerClient: CloudSchedulerClient,
  private val schedulerConfig: KairoGoogleCloudSchedulerConfig.Http,
) : RealGoogleCloudScheduler(
  cloudSchedulerClient = cloudSchedulerClient,
  schedulerConfig = schedulerConfig,
) {
  override fun buildJob(details: RestEndpointDetails, config: Config): com.google.cloud.scheduler.v1.Job =
    com.google.cloud.scheduler.v1.Job.newBuilder().apply {
      name = buildJobName(config.name).toString()
      description = buildJobDescription(config)
      @Suppress("DuplicatedCode")
      httpTarget = HttpTarget.newBuilder().apply {
        uri = buildUri(details)
        httpMethod = HttpMethod.valueOf(details.method.value)
        details.contentType?.let { putHeaders(HttpHeaders.ContentType, it.toString()) }
        details.accept?.let { putHeaders(HttpHeaders.Accept, it.toString()) }
        details.body?.let { body = ByteString.copyFrom(KtorServerMapper.json.writeValueAsBytes(it)) }
      }.build()
      schedule = config.schedule
    }.build()

  private fun buildUri(details: RestEndpointDetails): String =
    URLBuilder().takeFrom(schedulerConfig.baseUrl).takeFrom(details.path).buildString()
}

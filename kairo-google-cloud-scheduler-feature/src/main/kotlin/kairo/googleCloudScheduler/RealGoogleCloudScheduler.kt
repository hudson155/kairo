package kairo.googleCloudScheduler

import com.google.cloud.scheduler.v1.AppEngineHttpTarget
import com.google.cloud.scheduler.v1.AppEngineRouting
import com.google.cloud.scheduler.v1.CloudSchedulerClient
import com.google.cloud.scheduler.v1.CreateJobRequest
import com.google.cloud.scheduler.v1.DeleteJobRequest
import com.google.cloud.scheduler.v1.HttpMethod
import com.google.cloud.scheduler.v1.JobName
import com.google.cloud.scheduler.v1.LocationName
import com.google.protobuf.ByteString
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpHeaders
import kairo.googleCommon.await
import kairo.rest.KtorServerMapper
import kairo.rest.endpoint.RestEndpointDetails

private val logger: KLogger = KotlinLogging.logger {}

public class RealGoogleCloudScheduler(
  private val cloudSchedulerClient: CloudSchedulerClient,
  private val schedulerConfig: KairoGoogleCloudSchedulerConfig.Real,
) : GoogleCloudScheduler() {
  override suspend fun create(job: Job, config: Config) {
    logger.info { "Creating job: $job (config: $config)." }
    val request = CreateJobRequest.newBuilder().apply {
      parent = buildLocationName().toString()
      this.job = buildJob(job.details, config)
    }.build()
    cloudSchedulerClient.createJobCallable().futureCall(request).await()
  }

  override suspend fun delete(name: String) {
    logger.info { "Deleting job: $name." }
    val request = DeleteJobRequest.newBuilder().apply {
      this.name = buildJobName(name).toString()
    }.build()
    cloudSchedulerClient.deleteJobCallable().futureCall(request).await()
  }

  override fun close() {
    cloudSchedulerClient.close()
  }

  private fun buildLocationName(): LocationName =
    LocationName.of(schedulerConfig.projectId, schedulerConfig.location)

  private fun buildJob(details: RestEndpointDetails, config: Config): com.google.cloud.scheduler.v1.Job =
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

  private fun buildJobName(jobName: String): JobName {
    val job = listOfNotNull(
      schedulerConfig.jobName.prefix,
      jobName,
      schedulerConfig.jobName.suffix,
    ).joinToString("")
    return JobName.of(schedulerConfig.projectId, schedulerConfig.location, job)
  }

  private fun buildJobDescription(config: Config): String =
    config.description

  private fun buildAppEngineRouting(): AppEngineRouting =
    AppEngineRouting.newBuilder().apply {
      service = schedulerConfig.service
    }.build()
}

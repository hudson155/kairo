package kairo.googleCloudScheduler

import com.google.cloud.scheduler.v1.CloudSchedulerClient
import com.google.cloud.scheduler.v1.CreateJobRequest
import com.google.cloud.scheduler.v1.DeleteJobRequest
import com.google.cloud.scheduler.v1.JobName
import com.google.cloud.scheduler.v1.LocationName
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.googleCommon.await
import kairo.rest.endpoint.RestEndpointDetails

private val logger: KLogger = KotlinLogging.logger {}

public abstract class RealGoogleCloudScheduler(
  private val cloudSchedulerClient: CloudSchedulerClient,
  private val schedulerConfig: KairoGoogleCloudSchedulerConfig.Real,
) : GoogleCloudScheduler() {
  final override suspend fun create(job: Job, config: Config) {
    logger.info { "Creating job: $job (config: $config)." }
    val request = CreateJobRequest.newBuilder().apply {
      this.parent = buildLocationName().toString()
      this.job = buildJob(job.details, config)
    }.build()
    cloudSchedulerClient.createJobCallable().futureCall(request).await()
  }

  final override suspend fun delete(name: String) {
    logger.info { "Deleting job: $name." }
    val request = DeleteJobRequest.newBuilder().apply {
      this.name = buildJobName(name).toString()
    }.build()
    cloudSchedulerClient.deleteJobCallable().futureCall(request).await()
  }

  final override fun close() {
    cloudSchedulerClient.close()
  }

  private fun buildLocationName(): LocationName =
    LocationName.of(schedulerConfig.projectId, schedulerConfig.location)

  protected abstract fun buildJob(details: RestEndpointDetails, config: Config): com.google.cloud.scheduler.v1.Job

  protected fun buildJobName(jobName: String): JobName {
    val job = listOfNotNull(
      schedulerConfig.jobName.prefix,
      jobName,
      schedulerConfig.jobName.suffix,
    ).joinToString("")
    return JobName.of(schedulerConfig.projectId, schedulerConfig.location, job)
  }

  protected fun buildJobDescription(config: Config): String =
    config.description
}

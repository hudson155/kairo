package kairo.googleCloudScheduler

import com.google.cloud.scheduler.v1.CloudSchedulerClient
import com.google.inject.Inject
import com.google.inject.Singleton
import kairo.dependencyInjection.LazySingletonProvider

/**
 * There is a single global [JobCreator] instance in Kairo.
 */
@Singleton
public class JobCreatorProvider @Inject constructor(
  private val cloudSchedulerClient: CloudSchedulerClient,
  private val config: KairoGoogleCloudSchedulerConfig,
) : LazySingletonProvider<JobCreator>() {
  override fun create(): JobCreator =
    create(config)

  private fun create(config: KairoGoogleCloudSchedulerConfig): JobCreator =
    when (config) {
      is KairoGoogleCloudSchedulerConfig.Noop ->
        NoopJobCreator()
      is KairoGoogleCloudSchedulerConfig.Real ->
        RealJobCreator(
          cloudSchedulerClient = cloudSchedulerClient,
          schedulerConfig = config,
        )
    }
}

package kairo.googleCloudScheduler

import com.google.cloud.scheduler.v1.CloudSchedulerClient
import com.google.inject.Inject
import com.google.inject.Singleton
import kairo.dependencyInjection.LazySingletonProvider

/**
 * There is a single global [GoogleCloudScheduler] instance in Kairo.
 */
@Singleton
public class JobCreatorProvider @Inject constructor(
  private val cloudSchedulerClient: CloudSchedulerClient,
  private val config: KairoGoogleCloudSchedulerConfig,
) : LazySingletonProvider<GoogleCloudScheduler>() {
  override fun create(): GoogleCloudScheduler =
    create(config)

  private fun create(config: KairoGoogleCloudSchedulerConfig): GoogleCloudScheduler =
    when (config) {
      is KairoGoogleCloudSchedulerConfig.Noop ->
        NoopGoogleCloudScheduler()
      is KairoGoogleCloudSchedulerConfig.Real ->
        RealGoogleCloudScheduler(
          cloudSchedulerClient = cloudSchedulerClient,
          schedulerConfig = config,
        )
    }
}

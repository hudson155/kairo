package kairo.googleCloudScheduler

import com.google.cloud.scheduler.v1.CloudSchedulerClient
import com.google.inject.Inject
import com.google.inject.Singleton
import kairo.dependencyInjection.LazySingletonProvider

/**
 * There is a single global [CloudSchedulerClient] instance in Kairo.
 */
@Singleton
public class CloudSchedulerClientProvider @Inject constructor() : LazySingletonProvider<CloudSchedulerClient>() {
  override fun create(): CloudSchedulerClient =
    CloudSchedulerClient.create()
}

package kairo.googleCloudSchedulerRest

import com.google.inject.Inject
import kairo.googleCloudScheduler.googleCloudScheduler
import kairo.googleCloudTasks.GoogleCloudTasks
import kairo.rest.auth.Auth
import kairo.rest.auth.AuthProvider
import kairo.rest.handler.RestHandler

@Suppress("UseDataClass") // Handlers shouldn't be data classes.
internal class GoogleCloudSchedulerHandler @Inject constructor(
  private val googleCloudTasks: GoogleCloudTasks,
) {
  internal inner class CreateTask : RestHandler<GoogleCloudSchedulerApi.CreateTask, Unit>() {
    override suspend fun AuthProvider.auth(endpoint: GoogleCloudSchedulerApi.CreateTask): Auth.Result =
      googleCloudScheduler()

    override suspend fun handle(endpoint: GoogleCloudSchedulerApi.CreateTask) {
      googleCloudTasks.create(endpoint.body)
    }
  }
}

package io.limberapp.server

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.auth.auth.AuthSuperuser
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template

internal class RequiresPermissionHandler @Inject constructor(
) : EndpointHandler<TestApi.RequiresPermission, Unit>(
    template = TestApi.RequiresPermission::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): TestApi.RequiresPermission =
      TestApi.RequiresPermission

  override suspend fun Handler.handle(endpoint: TestApi.RequiresPermission) {
    auth(AuthSuperuser)
  }
}

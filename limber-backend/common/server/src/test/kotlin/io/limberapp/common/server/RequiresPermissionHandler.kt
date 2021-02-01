package io.limberapp.common.server

import io.ktor.application.ApplicationCall
import io.limberapp.common.auth.auth.AuthLimberPermission
import io.limberapp.common.permissions.limberPermissions.LimberPermission
import io.limberapp.common.restInterface.EndpointHandler
import io.limberapp.common.restInterface.template

internal class RequiresPermissionHandler : EndpointHandler<TestApi.RequiresPermission, Unit>(
    template = TestApi.RequiresPermission::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): TestApi.RequiresPermission =
      TestApi.RequiresPermission

  override suspend fun Handler.handle(endpoint: TestApi.RequiresPermission) {
    auth { AuthLimberPermission(LimberPermission.SUPERUSER) }
  }
}

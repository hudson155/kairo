package io.limberapp.backend.module.users.endpoint.user

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.service.user.UserService
import io.limberapp.common.auth.auth.AuthLimberPermission
import io.limberapp.common.permissions.limberPermissions.LimberPermission
import io.limberapp.common.restInterface.EndpointHandler
import io.limberapp.common.restInterface.template
import java.util.UUID

internal class DeleteUser @Inject constructor(
    private val userService: UserService,
) : EndpointHandler<UserApi.Delete, Unit>(
    template = UserApi.Delete::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): UserApi.Delete =
      UserApi.Delete(userGuid = call.getParam(UUID::class, "userGuid"))

  override suspend fun Handler.handle(endpoint: UserApi.Delete) {
    auth { AuthLimberPermission(LimberPermission.SUPERUSER) }
    userService.delete(endpoint.userGuid)
  }
}

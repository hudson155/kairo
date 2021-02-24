package io.limberapp.endpoint.user

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.auth.auth.AuthLimberPermission
import io.limberapp.api.user.UserApi
import io.limberapp.service.user.UserService
import io.limberapp.permissions.limber.LimberPermission
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
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

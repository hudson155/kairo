package io.limberapp.endpoint.user

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.user.UserApi
import io.limberapp.auth.auth.AuthSuperuser
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.user.UserService
import java.util.UUID

internal class DeleteUser @Inject constructor(
    private val userService: UserService,
) : EndpointHandler<UserApi.Delete, Unit>(
    template = UserApi.Delete::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): UserApi.Delete =
      UserApi.Delete(userGuid = call.getParam(UUID::class, "userGuid"))

  override suspend fun Handler.handle(endpoint: UserApi.Delete) {
    auth(AuthSuperuser)
    userService.delete(endpoint.userGuid)
  }
}

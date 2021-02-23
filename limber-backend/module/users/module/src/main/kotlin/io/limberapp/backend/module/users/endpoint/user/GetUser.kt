package io.limberapp.backend.module.users.endpoint.user

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.exception.user.UserNotFound
import io.limberapp.backend.module.users.mapper.user.UserMapper
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.service.user.UserService
import io.limberapp.common.auth.auth.AuthUser
import io.limberapp.common.restInterface.EndpointHandler
import io.limberapp.common.restInterface.template
import java.util.UUID

internal class GetUser @Inject constructor(
    private val userService: UserService,
    private val userMapper: UserMapper,
) : EndpointHandler<UserApi.Get, UserRep.Complete>(
    template = UserApi.Get::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): UserApi.Get =
      UserApi.Get(userGuid = call.getParam(UUID::class, "userGuid"))

  override suspend fun Handler.handle(endpoint: UserApi.Get): UserRep.Complete {
    auth { AuthUser(endpoint.userGuid) }
    val user = userService[endpoint.userGuid] ?: throw UserNotFound()
    return userMapper.completeRep(user)
  }
}

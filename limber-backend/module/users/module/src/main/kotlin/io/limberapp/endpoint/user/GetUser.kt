package io.limberapp.endpoint.user

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.auth.auth.AuthUser
import io.limberapp.api.user.UserApi
import io.limberapp.exception.user.UserNotFound
import io.limberapp.mapper.user.UserMapper
import io.limberapp.rep.user.UserRep
import io.limberapp.service.user.UserService
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
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

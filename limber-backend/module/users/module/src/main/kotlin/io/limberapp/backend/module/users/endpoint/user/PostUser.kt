package io.limberapp.backend.module.users.endpoint.user

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.mapper.user.UserMapper
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.service.user.UserService
import io.limberapp.common.auth.auth.AuthLimberPermission
import io.limberapp.common.permissions.limberPermissions.LimberPermission
import io.limberapp.common.restInterface.EndpointHandler
import io.limberapp.common.restInterface.template

internal class PostUser @Inject constructor(
    private val userService: UserService,
    private val userMapper: UserMapper,
) : EndpointHandler<UserApi.Post, UserRep.Complete>(
    template = UserApi.Post::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): UserApi.Post =
      UserApi.Post(rep = call.getAndValidateBody())

  override suspend fun Handler.handle(endpoint: UserApi.Post): UserRep.Complete {
    val rep = endpoint.rep.required()
    auth { AuthLimberPermission(LimberPermission.SUPERUSER) }
    val user = userService.create(userMapper.model(rep))
    return userMapper.completeRep(user)
  }
}

package io.limberapp.endpoint.user

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.user.UserApi
import io.limberapp.auth.auth.AuthSuperuser
import io.limberapp.mapper.user.UserMapper
import io.limberapp.rep.user.UserRep
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.user.UserService

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
    auth(AuthSuperuser)
    val user = userService.create(userMapper.model(rep))
    return userMapper.completeRep(user)
  }
}

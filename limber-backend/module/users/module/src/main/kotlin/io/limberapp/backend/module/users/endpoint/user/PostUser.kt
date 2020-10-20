package io.limberapp.backend.module.users.endpoint.user

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.authorization.AuthAccountRole
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.users.api.account.UserApi
import io.limberapp.backend.module.users.mapper.account.UserMapper
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.service.account.UserService
import io.limberapp.common.restInterface.template
import io.limberapp.permissions.AccountRole

internal class PostUser @Inject constructor(
    application: Application,
    private val userService: UserService,
    private val userMapper: UserMapper,
) : LimberApiEndpoint<UserApi.Post, UserRep.Complete>(
    application = application,
    endpointTemplate = UserApi.Post::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = UserApi.Post(
      rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: UserApi.Post): UserRep.Complete {
    val rep = command.rep.required()
    auth { AuthAccountRole(AccountRole.SUPERUSER) }
    val user = userService.create(userMapper.model(rep))
    return userMapper.completeRep(user)
  }
}

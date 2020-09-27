package io.limberapp.backend.module.users.endpoint.user

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.mapper.account.UserMapper
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.service.account.UserService
import io.limberapp.common.restInterface.template
import java.util.*

internal class GetUser @Inject constructor(
  application: Application,
  private val userService: UserService,
  private val userMapper: UserMapper,
) : LimberApiEndpoint<UserApi.Get, UserRep.Complete>(
  application = application,
  endpointTemplate = UserApi.Get::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = UserApi.Get(
    userGuid = call.parameters.getAsType(UUID::class, "userGuid")
  )

  override suspend fun Handler.handle(command: UserApi.Get): UserRep.Complete {
    Authorization.User(command.userGuid).authorize()
    val user = userService.get(command.userGuid) ?: throw UserNotFound()
    return userMapper.completeRep(user)
  }
}

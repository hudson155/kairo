package io.limberapp.backend.module.users.endpoint.user

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.mapper.account.UserMapper
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.service.account.UserService

/**
 * Returns a single user with the given email address.
 */
internal class GetUserByEmailAddress @Inject constructor(
  application: Application,
  servingConfig: ServingConfig,
  private val userService: UserService,
  private val userMapper: UserMapper
) : LimberApiEndpoint<UserApi.GetByEmailAddress, UserRep.Complete>(
  application = application,
  pathPrefix = servingConfig.apiPathPrefix,
  endpointTemplate = UserApi.GetByEmailAddress::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = UserApi.GetByEmailAddress(
    emailAddress = call.parameters.getAsType(String::class, "emailAddress")
  )

  override suspend fun Handler.handle(command: UserApi.GetByEmailAddress): UserRep.Complete {
    Authorization.AnyJwt.authorize()
    val user = userService.getByEmailAddress(command.emailAddress) ?: throw UserNotFound()
    Authorization.User(user.guid).authorize()
    return userMapper.completeRep(user)
  }
}

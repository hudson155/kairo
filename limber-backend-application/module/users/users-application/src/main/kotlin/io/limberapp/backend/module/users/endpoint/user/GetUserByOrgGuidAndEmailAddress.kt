package io.limberapp.backend.module.users.endpoint.user

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import com.piperframework.types.UUID
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
 * Returns a single user in the given org with the given email address.
 */
internal class GetUserByOrgGuidAndEmailAddress @Inject constructor(
  application: Application,
  servingConfig: ServingConfig,
  private val userService: UserService,
  private val userMapper: UserMapper
) : LimberApiEndpoint<UserApi.GetByOrgGuidAndEmailAddress, UserRep.Complete>(
  application = application,
  pathPrefix = servingConfig.apiPathPrefix,
  endpointTemplate = UserApi.GetByOrgGuidAndEmailAddress::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = UserApi.GetByOrgGuidAndEmailAddress(
    orgGuid = call.parameters.getAsType(UUID::class, "orgGuid"),
    emailAddress = call.parameters.getAsType(String::class, "emailAddress")
  )

  override suspend fun Handler.handle(command: UserApi.GetByOrgGuidAndEmailAddress): UserRep.Complete {
    Authorization.OrgMember(command.orgGuid).authorize()
    val user = userService.getByOrgGuidAndEmailAddress(command.orgGuid, command.emailAddress)
    Authorization.User(user?.guid).authorize()
    return userMapper.completeRep(user ?: throw UserNotFound())
  }
}

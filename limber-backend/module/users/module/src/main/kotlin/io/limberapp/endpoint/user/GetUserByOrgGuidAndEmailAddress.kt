package io.limberapp.endpoint.user

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.user.UserApi
import io.limberapp.auth.auth.AuthOrgMember
import io.limberapp.auth.auth.AuthUser
import io.limberapp.exception.user.UserNotFound
import io.limberapp.mapper.user.UserMapper
import io.limberapp.rep.user.UserRep
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.user.UserService
import java.util.UUID

internal class GetUserByOrgGuidAndEmailAddress @Inject constructor(
    private val userService: UserService,
    private val userMapper: UserMapper,
) : EndpointHandler<UserApi.GetByOrgGuidAndEmailAddress, UserRep.Complete>(
    template = UserApi.GetByOrgGuidAndEmailAddress::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): UserApi.GetByOrgGuidAndEmailAddress =
      UserApi.GetByOrgGuidAndEmailAddress(
          orgGuid = call.getParam(UUID::class, "orgGuid"),
          emailAddress = call.getParam(String::class, "emailAddress"),
      )

  override suspend fun Handler.handle(
      endpoint: UserApi.GetByOrgGuidAndEmailAddress,
  ): UserRep.Complete {
    auth(AuthOrgMember(endpoint.orgGuid))
    val user = userService.getByEmailAddress(endpoint.orgGuid, endpoint.emailAddress)
        ?: throw UserNotFound()
    auth(AuthUser(user.guid))
    return userMapper.completeRep(user)
  }
}

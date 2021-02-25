package io.limberapp.endpoint.user

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.user.UserApi
import io.limberapp.auth.auth.AuthOrgMember
import io.limberapp.mapper.user.UserMapper
import io.limberapp.rep.user.UserRep
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.user.UserService
import java.util.UUID

internal class GetUsersByOrgGuid @Inject constructor(
    private val userService: UserService,
    private val userMapper: UserMapper,
) : EndpointHandler<UserApi.GetByOrgGuid, Set<UserRep.Summary>>(
    template = UserApi.GetByOrgGuid::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): UserApi.GetByOrgGuid =
      UserApi.GetByOrgGuid(orgGuid = call.getParam(UUID::class, "orgGuid"))

  override suspend fun Handler.handle(endpoint: UserApi.GetByOrgGuid): Set<UserRep.Summary> {
    auth(AuthOrgMember(endpoint.orgGuid))
    val users = userService.getByOrgGuid(endpoint.orgGuid)
    return users.map { userMapper.summaryRep(it) }.toSet()
  }
}

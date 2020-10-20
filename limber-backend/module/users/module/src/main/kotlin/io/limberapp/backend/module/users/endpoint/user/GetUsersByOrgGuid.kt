package io.limberapp.backend.module.users.endpoint.user

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.authorization.AuthOrgMember
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.users.api.account.UserApi
import io.limberapp.backend.module.users.mapper.account.UserMapper
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.service.account.UserService
import io.limberapp.common.restInterface.template
import java.util.*

internal class GetUsersByOrgGuid @Inject constructor(
    application: Application,
    private val userService: UserService,
    private val userMapper: UserMapper,
) : LimberApiEndpoint<UserApi.GetByOrgGuid, Set<UserRep.Summary>>(
    application = application,
    endpointTemplate = UserApi.GetByOrgGuid::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = UserApi.GetByOrgGuid(
      orgGuid = call.parameters.getAsType(UUID::class, "orgGuid")
  )

  override suspend fun Handler.handle(command: UserApi.GetByOrgGuid): Set<UserRep.Summary> {
    auth { AuthOrgMember(command.orgGuid) }
    val users = userService.getByOrgGuid(command.orgGuid)
    return users.map { userMapper.summaryRep(it) }.toSet()
  }
}

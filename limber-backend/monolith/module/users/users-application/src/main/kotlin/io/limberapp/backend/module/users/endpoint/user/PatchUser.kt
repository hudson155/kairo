package io.limberapp.backend.module.users.endpoint.user

import com.google.inject.Inject
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermission
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.mapper.account.UserMapper
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.service.account.UserService
import java.util.*

internal class PatchUser @Inject constructor(
  application: Application,
  private val userService: UserService,
  private val userMapper: UserMapper,
) : LimberApiEndpoint<UserApi.Patch, UserRep.Complete>(
  application = application,
  endpointTemplate = UserApi.Patch::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = UserApi.Patch(
    userGuid = call.parameters.getAsType(UUID::class, "userGuid"),
    rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: UserApi.Patch): UserRep.Complete {
    val rep = command.rep.required()
    Authorization.User(command.userGuid).authorize()
    Authorization.WithOrgPermission(OrgPermission.MODIFY_OWN_METADATA, ignoreOrgGuid = true).authorize()
    val user = userService.update(command.userGuid, userMapper.update(rep))
    return userMapper.completeRep(user)
  }
}

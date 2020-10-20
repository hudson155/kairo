package io.limberapp.backend.module.users.endpoint.user

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Auth
import io.limberapp.backend.authorization.authorization.AuthAccountRole
import io.limberapp.backend.authorization.authorization.AuthOrgMember
import io.limberapp.backend.authorization.authorization.AuthUser
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.users.api.account.UserApi
import io.limberapp.backend.module.users.mapper.account.UserMapper
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.service.account.UserService
import io.limberapp.common.restInterface.template
import io.limberapp.permissions.AccountRole
import io.limberapp.permissions.orgPermissions.OrgPermission
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
    auth {
      Auth.All(
          AuthUser(command.userGuid),
          AuthOrgMember(null, permission = OrgPermission.MODIFY_OWN_METADATA),
          if (rep.specifiesAccountRoleAdditions) AuthAccountRole(AccountRole.SUPERUSER) else null,
      )
    }
    val user = userService.update(command.userGuid, userMapper.update(rep))
    return userMapper.completeRep(user)
  }
}

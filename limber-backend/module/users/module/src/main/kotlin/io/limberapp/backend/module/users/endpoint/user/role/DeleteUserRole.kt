package io.limberapp.backend.module.users.endpoint.user.role

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.users.api.user.role.UserRoleApi
import io.limberapp.backend.module.users.exception.account.UserDoesNotHaveRole
import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.model.account.UserModel
import io.limberapp.backend.module.users.service.account.UserService
import io.limberapp.common.restInterface.template
import java.util.*

internal class DeleteUserRole @Inject constructor(
  application: Application,
  private val userService: UserService,
) : LimberApiEndpoint<UserRoleApi.Delete, Unit>(
  application = application,
  endpointTemplate = UserRoleApi.Delete::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = UserRoleApi.Delete(
    userGuid = call.parameters.getAsType(UUID::class, "userGuid"),
    role = call.parameters.getAsType(JwtRole::class, "role")
  )

  override suspend fun Handler.handle(command: UserRoleApi.Delete) {
    Authorization.Role(JwtRole.SUPERUSER).authorize()
    val user = userService.get(command.userGuid) ?: throw UserNotFound()
    if (!user.hasRole(command.role)) throw UserDoesNotHaveRole()
    userService.update(command.userGuid, UserModel.Update.fromRole(command.role, false))
  }
}

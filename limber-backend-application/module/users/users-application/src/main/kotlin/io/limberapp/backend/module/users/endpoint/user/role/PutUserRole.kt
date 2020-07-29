package io.limberapp.backend.module.users.endpoint.user.role

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.users.api.user.role.UserRoleApi
import io.limberapp.backend.module.users.model.account.UserModel
import io.limberapp.backend.module.users.service.account.UserService
import java.util.*

internal class PutUserRole @Inject constructor(
  application: Application,
  servingConfig: ServingConfig,
  private val userService: UserService
) : LimberApiEndpoint<UserRoleApi.Put, Unit>(
  application = application,
  pathPrefix = servingConfig.apiPathPrefix,
  endpointTemplate = UserRoleApi.Put::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = UserRoleApi.Put(
    userGuid = call.parameters.getAsType(UUID::class, "userGuid"),
    role = call.parameters.getAsType(JwtRole::class, "role")
  )

  override suspend fun Handler.handle(command: UserRoleApi.Put) {
    Authorization.Role(JwtRole.SUPERUSER).authorize()
    userService.update(command.userGuid, UserModel.Update.fromRole(command.role, true))
  }
}

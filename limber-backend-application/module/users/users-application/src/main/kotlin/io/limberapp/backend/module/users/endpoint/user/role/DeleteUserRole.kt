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
import io.limberapp.backend.module.users.exception.account.UserDoesNotHaveRole
import io.limberapp.backend.module.users.model.account.UserModel
import io.limberapp.backend.module.users.service.account.UserService
import java.util.UUID

/**
 * Revokes a certain role from the user. Roles are system-wide, NOT org-wide.
 */
internal class DeleteUserRole @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val userService: UserService
) : LimberApiEndpoint<UserRoleApi.Delete, Unit>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = UserRoleApi.Delete::class.template()
) {

    override suspend fun determineCommand(call: ApplicationCall) = UserRoleApi.Delete(
        userId = call.parameters.getAsType(UUID::class, "userId"),
        role = call.parameters.getAsType(JwtRole::class, "role")
    )

    override suspend fun Handler.handle(command: UserRoleApi.Delete) {
        Authorization.Role(JwtRole.SUPERUSER).authorize()
        if (userService.get(command.userId)?.hasRole(command.role) == false) throw UserDoesNotHaveRole(command.role)
        userService.update(command.userId, UserModel.Update.fromRole(command.role, false))
    }
}

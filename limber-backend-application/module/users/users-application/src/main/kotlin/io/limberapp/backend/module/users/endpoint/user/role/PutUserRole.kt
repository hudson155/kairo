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
import io.limberapp.backend.module.users.exception.account.UserAlreadyHasRole
import io.limberapp.backend.module.users.service.account.UserService
import java.util.UUID

/**
 * Gives the user a certain role. Roles are system-wide, NOT org-wide.
 */
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
        userId = call.parameters.getAsType(UUID::class, "userId"),
        role = call.parameters.getAsType(JwtRole::class, "role")
    )

    override suspend fun Handler.handle(command: UserRoleApi.Put) {
        Authorization.Role(JwtRole.SUPERUSER).authorize()
        try {
            userService.addRole(command.userId, command.role)
        } catch (_: UserAlreadyHasRole) {
            return
        }
    }
}

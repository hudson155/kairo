package io.limberapp.backend.module.users.endpoint.user.role

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.StringComponent
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.VariableComponent
import com.piperframework.endpoint.command.AbstractCommand
import com.piperframework.module.annotation.Service
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.users.exception.conflict.UserAlreadyHasRole
import io.limberapp.backend.module.users.service.user.UserService
import java.util.UUID

/**
 * Gives the user a certain role. Roles are system-wide, NOT org-wide.
 */
internal class AddUserRole @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    @Service private val userService: UserService
) : LimberApiEndpoint<AddUserRole.Command, Unit>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val userId: UUID,
        val roleName: JwtRole
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        userId = call.parameters.getAsType(UUID::class, userId),
        roleName = call.parameters.getAsType(JwtRole::class, roleName)
    )

    override suspend fun Handler.handle(command: Command) {
        Authorization.Superuser.authorize()
        try {
            userService.addRole(command.userId, command.roleName)
        } catch (_: UserAlreadyHasRole) {
            return
        }
    }

    companion object {
        const val userId = "userId"
        const val roleName = "roleName"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Put,
            pathTemplate = listOf(
                StringComponent("users"),
                VariableComponent(userId),
                StringComponent("roles"),
                VariableComponent(roleName)
            )
        )
    }
}

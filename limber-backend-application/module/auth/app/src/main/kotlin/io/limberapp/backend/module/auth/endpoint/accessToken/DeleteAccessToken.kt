package io.limberapp.backend.module.auth.endpoint.accessToken

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.StringComponent
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.VariableComponent
import com.piperframework.endpoint.command.AbstractCommand
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.service.accessToken.AccessTokenService
import java.util.UUID

/**
 * Deletes the given access token from the given user.
 */
internal class DeleteAccessToken @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val accessTokenService: AccessTokenService
) : LimberApiEndpoint<DeleteAccessToken.Command, Unit>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val userId: UUID,
        val accessTokenId: UUID
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        userId = call.parameters.getAsType(UUID::class, userId),
        accessTokenId = call.parameters.getAsType(UUID::class, accessTokenId)
    )

    override suspend fun Handler.handle(command: Command) {
        Authorization.Role(JwtRole.SUPERUSER).authorize()
        accessTokenService.delete(command.userId, command.accessTokenId)
    }

    companion object {
        const val userId = "userId"
        const val accessTokenId = "accessTokenId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Delete,
            pathTemplate = listOf(
                StringComponent("users"),
                VariableComponent(userId),
                StringComponent("access-tokens"),
                VariableComponent(accessTokenId)
            )
        )
    }
}

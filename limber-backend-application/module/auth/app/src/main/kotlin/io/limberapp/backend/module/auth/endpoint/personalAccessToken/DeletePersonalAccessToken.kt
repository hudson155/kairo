package io.limberapp.backend.module.auth.endpoint.personalAccessToken

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.command.AbstractCommand
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.service.personalAccessToken.PersonalAccessTokenService
import java.util.UUID

/**
 * Deletes the given personal access token from the given user.
 */
internal class DeletePersonalAccessToken @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val personalAccessTokenService: PersonalAccessTokenService
) : LimberApiEndpoint<DeletePersonalAccessToken.Command, Unit>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val userId: UUID,
        val personalAccessTokenId: UUID
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        userId = call.parameters.getAsType(UUID::class, userId),
        personalAccessTokenId = call.parameters.getAsType(UUID::class, personalAccessTokenId)
    )

    override fun authorization(command: Command) = Authorization.Superuser

    override suspend fun handler(command: Command) {
        personalAccessTokenService.delete(command.userId, command.personalAccessTokenId)
    }

    companion object {
        const val userId = "userId"
        const val personalAccessTokenId = "personalAccessTokenId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Delete,
            pathTemplate = "/users/{$userId}/personal-access-tokens/{$personalAccessTokenId}"
        )
    }
}

package io.limberapp.backend.module.auth.endpoint.personalAccessToken

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.module.auth.mapper.api.personalAccessToken.PersonalAccessTokenMapper
import io.limberapp.backend.module.auth.service.personalAccessToken.PersonalAccessTokenService
import io.limberapp.framework.config.serving.ServingConfig
import io.limberapp.framework.endpoint.ApiEndpoint
import io.limberapp.framework.endpoint.EndpointConfig
import io.limberapp.framework.endpoint.authorization.Authorization
import io.limberapp.framework.endpoint.command.AbstractCommand
import java.util.UUID

/**
 * Deletes the given personal access token from the given user.
 */
internal class DeletePersonalAccessToken @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val personalAccessTokenService: PersonalAccessTokenService,
    private val personalAccessTokenMapper: PersonalAccessTokenMapper
) : ApiEndpoint<DeletePersonalAccessToken.Command, Unit>(
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

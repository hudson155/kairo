package io.limberapp.backend.module.auth.endpoint.personalAccessToken

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.module.auth.mapper.api.personalAccessToken.PersonalAccessTokenMapper
import io.limberapp.backend.module.auth.rep.personalAccessToken.PersonalAccessTokenRep
import io.limberapp.backend.module.auth.service.personalAccessToken.PersonalAccessTokenService
import io.limberapp.framework.config.serving.ServingConfig
import io.limberapp.framework.endpoint.ApiEndpoint
import io.limberapp.framework.endpoint.EndpointConfig
import io.limberapp.framework.endpoint.authorization.Authorization
import io.limberapp.framework.endpoint.command.AbstractCommand
import java.util.UUID

/**
 * Creates a personal access token for the given user. Note that this endpoint returns a "one time
 * use" rep. This means that the token itself will only be returned by this endpoint, immediately
 * after it is created. The user must record the token appropriately, because it cannot be returned
 * again. If a new token is needed, the user can always delete the existing token and create
 * another.
 */
internal class CreatePersonalAccessToken @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val personalAccessTokenService: PersonalAccessTokenService,
    private val personalAccessTokenMapper: PersonalAccessTokenMapper
) : ApiEndpoint<CreatePersonalAccessToken.Command, PersonalAccessTokenRep.OneTimeUse>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val userId: UUID
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        userId = call.parameters.getAsType(UUID::class, userId)
    )

    override fun authorization(command: Command) = Authorization.Superuser

    override suspend fun handler(command: Command): PersonalAccessTokenRep.OneTimeUse {
        val model = personalAccessTokenMapper.model(command.userId)
        personalAccessTokenService.create(model)
        return personalAccessTokenMapper.oneTimeUseRep(model)
    }

    companion object {
        const val userId = "userId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Post,
            pathTemplate = "/users/{$userId}/personal-access-tokens"
        )
    }
}

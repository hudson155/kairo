package io.limberapp.backend.module.auth.endpoint.personalAccessToken

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
import io.limberapp.backend.module.auth.mapper.api.personalAccessToken.PersonalAccessTokenMapper
import io.limberapp.backend.module.auth.rep.personalAccessToken.PersonalAccessTokenRep
import io.limberapp.backend.module.auth.service.personalAccessToken.PersonalAccessTokenService
import java.util.UUID

/**
 * Creates a personal access token for the given user. Note that this endpoint returns a "one time use" rep. This means
 * that the token itself will only be returned by this endpoint, immediately after it is created. The user must record
 * the token appropriately, because it cannot be returned again. If a new token is needed, the user can always delete
 * the existing token and create another.
 */
internal class CreatePersonalAccessToken @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    @Service private val personalAccessTokenService: PersonalAccessTokenService,
    private val personalAccessTokenMapper: PersonalAccessTokenMapper
) : LimberApiEndpoint<CreatePersonalAccessToken.Command, PersonalAccessTokenRep.OneTimeUse>(
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

    override suspend fun Handler.handle(command: Command): PersonalAccessTokenRep.OneTimeUse {
        Authorization.Role(JwtRole.SUPERUSER).authorize()
        val model = personalAccessTokenMapper.model(command.userId)
        personalAccessTokenService.create(model)
        return personalAccessTokenMapper.oneTimeUseRep(model)
    }

    companion object {
        const val userId = "userId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Post,
            pathTemplate = listOf(
                StringComponent("users"),
                VariableComponent(userId),
                StringComponent("personal-access-tokens")
            )
        )
    }
}

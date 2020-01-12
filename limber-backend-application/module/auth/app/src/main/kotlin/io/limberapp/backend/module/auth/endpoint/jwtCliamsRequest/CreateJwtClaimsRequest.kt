package io.limberapp.backend.module.auth.endpoint.jwtCliamsRequest

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.StringComponent
import com.piperframework.endpoint.command.AbstractCommand
import com.piperframework.module.annotation.Service
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.mapper.api.jwtClaimsRequest.JwtClaimsRequestMapper
import io.limberapp.backend.module.auth.rep.jwtClaimsRequest.JwtClaimsRequestRep
import io.limberapp.backend.module.auth.service.jwtClaimsRequest.JwtClaimsRequestService

/**
 * Creates something called a "JWT claims request", which is never persisted anywhere. Instead, think of this claims
 * request as a transitive request that is resolved when the HTTP request is resolved. The result of the JWT claims
 * request is an object containing JWT claims that should be included in JWTs. Auth0 creates JWT claims requests and
 * uses the results every time it issues a JWT.
 */
internal class CreateJwtClaimsRequest @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    @Service private val jwtClaimsRequestService: JwtClaimsRequestService,
    private val jwtClaimsRequestMapper: JwtClaimsRequestMapper
) : LimberApiEndpoint<CreateJwtClaimsRequest.Command, JwtClaimsRequestRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val creationRep: JwtClaimsRequestRep.Creation
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        creationRep = call.getAndValidateBody()
    )

    override suspend fun Handler.handle(command: Command): JwtClaimsRequestRep.Complete {
        Authorization.Superuser.authorize()
        val requestModel = jwtClaimsRequestMapper.model(command.creationRep)
        val claimsModel = jwtClaimsRequestService.requestJwtClaims(requestModel)
        return jwtClaimsRequestMapper.completeRep(claimsModel)
    }

    companion object {
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Post,
            pathTemplate = listOf(StringComponent("jwt-claims-request"))
        )
    }
}

package io.limberapp.backend.endpoint

import io.ktor.application.Application
import io.limberapp.backend.authorization.principal.JwtPrincipal
import io.limberapp.common.endpoint.ApiEndpoint
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.common.restInterface.LimberEndpointTemplate

abstract class LimberApiEndpoint<Endpoint : LimberEndpoint, ResponseType : Any>(
    application: Application,
    endpointTemplate: LimberEndpointTemplate,
) : ApiEndpoint<JwtPrincipal, Endpoint, ResponseType>(application, endpointTemplate)

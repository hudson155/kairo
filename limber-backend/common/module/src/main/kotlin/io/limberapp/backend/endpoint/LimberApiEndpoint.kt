package io.limberapp.backend.endpoint

import io.ktor.application.Application
import io.limberapp.common.endpoint.ApiEndpoint
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.common.restInterface.LimberEndpointTemplate

abstract class LimberApiEndpoint<Endpoint : LimberEndpoint, ResponseType : Any>(
    application: Application,
    endpointTemplate: LimberEndpointTemplate,
) : ApiEndpoint<Endpoint, ResponseType>(application, endpointTemplate)

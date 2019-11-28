package io.limberapp.backend.endpoint

import com.piperframework.endpoint.ApiEndpoint
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.command.AbstractCommand
import io.ktor.application.Application
import io.limberapp.backend.authorization.principal.Jwt

abstract class LimberApiEndpoint<Command : AbstractCommand, ResponseType : Any?>(
    application: Application,
    pathPrefix: String,
    endpointConfig: EndpointConfig
) : ApiEndpoint<Jwt, Command, ResponseType>(application, pathPrefix, endpointConfig)

package io.limberapp.backend.endpoint

import com.piperframework.endpoint.ApiEndpoint
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.command.AbstractCommand
import com.piperframework.restInterface.PiperEndpointTemplate
import com.piperframework.util.unknownValue
import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.limberapp.backend.authorization.principal.Jwt

abstract class LimberApiEndpoint<Command : AbstractCommand, ResponseType : Any>(
    application: Application,
    pathPrefix: String,
    endpointTemplate: PiperEndpointTemplate
) : ApiEndpoint<Jwt, Command, ResponseType>(application, pathPrefix, endpointTemplate) {

    @Deprecated("API Transition")
    constructor(
        application: Application,
        pathPrefix: String,
        endpointConfig: EndpointConfig
    ) : this(
        application, pathPrefix, PiperEndpointTemplate(
            httpMethod = when (endpointConfig.httpMethod) {
                HttpMethod.Delete -> com.piperframework.restInterface.HttpMethod.DELETE
                HttpMethod.Get -> com.piperframework.restInterface.HttpMethod.GET
                HttpMethod.Patch -> com.piperframework.restInterface.HttpMethod.PATCH
                HttpMethod.Post -> com.piperframework.restInterface.HttpMethod.POST
                HttpMethod.Put -> com.piperframework.restInterface.HttpMethod.PUT
                else -> unknownValue("HttpMethod", endpointConfig.httpMethod)
            },
            pathTemplate = endpointConfig.pathTemplate
        )
    )
}

package io.limberapp.backend.endpoint

import com.piperframework.endpoint.ApiEndpoint
import com.piperframework.restInterface.PiperEndpoint
import com.piperframework.restInterface.PiperEndpointTemplate
import io.ktor.application.Application
import io.limberapp.backend.authorization.principal.Jwt

abstract class LimberApiEndpoint<Endpoint : PiperEndpoint, ResponseType : Any>(
  application: Application,
  pathPrefix: String,
  endpointTemplate: PiperEndpointTemplate
) : ApiEndpoint<Jwt, Endpoint, ResponseType>(application, pathPrefix, endpointTemplate)

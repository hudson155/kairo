package io.limberapp.endpoint.jwtCliamsRequest

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.jwtClaimsRequest.JwtClaimsRequestApi
import io.limberapp.auth.auth.AuthIdentityProvider
import io.limberapp.auth.jwt.Jwt
import io.limberapp.mapper.jwtClaimsRequest.JwtClaimsRequestMapper
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.jwtClaimsRequest.JwtClaimsRequestService

internal class PostJwtClaimsRequest @Inject constructor(
    private val jwtClaimsRequestService: JwtClaimsRequestService,
    private val jwtClaimsRequestMapper: JwtClaimsRequestMapper,
) : EndpointHandler<JwtClaimsRequestApi.Post, Jwt>(
    template = JwtClaimsRequestApi.Post::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): JwtClaimsRequestApi.Post =
      JwtClaimsRequestApi.Post(rep = call.getAndValidateBody())

  override suspend fun Handler.handle(endpoint: JwtClaimsRequestApi.Post): Jwt {
    val rep = endpoint.rep.required()
    auth(AuthIdentityProvider)
    val jwt = jwtClaimsRequestService.requestJwtClaims(jwtClaimsRequestMapper.model(rep))
    return jwtClaimsRequestMapper.completeRep(jwt)
  }
}

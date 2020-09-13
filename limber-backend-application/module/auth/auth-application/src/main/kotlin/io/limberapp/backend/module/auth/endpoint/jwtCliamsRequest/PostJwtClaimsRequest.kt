package io.limberapp.backend.module.auth.endpoint.jwtCliamsRequest

import com.google.inject.Inject
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.jwtClaimsRequest.JwtClaimsRequestApi
import io.limberapp.backend.module.auth.mapper.jwtClaimsRequest.JwtClaimsRequestMapper
import io.limberapp.backend.module.auth.rep.jwtClaimsRequest.JwtClaimsRequestRep
import io.limberapp.backend.module.auth.service.jwtClaimsRequest.JwtClaimsRequestService

internal class PostJwtClaimsRequest @Inject constructor(
  application: Application,
  private val jwtClaimsRequestService: JwtClaimsRequestService,
  private val jwtClaimsRequestMapper: JwtClaimsRequestMapper,
) : LimberApiEndpoint<JwtClaimsRequestApi.Post, JwtClaimsRequestRep.Complete>(
  application = application,
  endpointTemplate = JwtClaimsRequestApi.Post::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = JwtClaimsRequestApi.Post(
    rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: JwtClaimsRequestApi.Post): JwtClaimsRequestRep.Complete {
    val rep = command.rep.required()
    Authorization.Role(JwtRole.IDENTITY_PROVIDER).authorize()
    val claims = jwtClaimsRequestService.requestJwtClaims(jwtClaimsRequestMapper.model(rep))
    return jwtClaimsRequestMapper.completeRep(claims)
  }
}

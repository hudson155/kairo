package io.limberapp.backend.module.auth.endpoint.jwtCliamsRequest

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.authorization.AuthAccountRole
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.jwtClaimsRequest.JwtClaimsRequestApi
import io.limberapp.backend.module.auth.mapper.jwtClaimsRequest.JwtClaimsRequestMapper
import io.limberapp.backend.module.auth.service.jwtClaimsRequest.JwtClaimsRequestService
import io.limberapp.common.auth.jwt.Jwt
import io.limberapp.common.permissions.AccountRole
import io.limberapp.common.restInterface.template

internal class PostJwtClaimsRequest @Inject constructor(
    application: Application,
    private val jwtClaimsRequestService: JwtClaimsRequestService,
    private val jwtClaimsRequestMapper: JwtClaimsRequestMapper,
) : LimberApiEndpoint<JwtClaimsRequestApi.Post, Jwt>(
    application = application,
    endpointTemplate = JwtClaimsRequestApi.Post::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = JwtClaimsRequestApi.Post(
      rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: JwtClaimsRequestApi.Post): Jwt {
    val rep = command.rep.required()
    auth { AuthAccountRole(AccountRole.IDENTITY_PROVIDER) }
    return jwtClaimsRequestService.requestJwtClaims(jwtClaimsRequestMapper.model(rep))
  }
}

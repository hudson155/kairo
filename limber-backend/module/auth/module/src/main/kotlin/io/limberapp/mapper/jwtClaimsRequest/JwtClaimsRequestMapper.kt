package io.limberapp.mapper.jwtClaimsRequest

import com.google.inject.Inject
import io.limberapp.auth.jwt.Jwt
import io.limberapp.model.jwtClaimsRequest.JwtClaimsRequestModel
import io.limberapp.rep.jwtClaimsRequest.JwtClaimsRequestRep

internal class JwtClaimsRequestMapper @Inject constructor() {
  fun model(rep: JwtClaimsRequestRep.Creation): JwtClaimsRequestModel =
      JwtClaimsRequestModel(
          auth0ClientId = rep.auth0ClientId,
          fullName = rep.fullName,
          emailAddress = rep.emailAddress,
          profilePhotoUrl = rep.profilePhotoUrl,
      )

  fun completeRep(jwt: Jwt): Jwt = jwt
}

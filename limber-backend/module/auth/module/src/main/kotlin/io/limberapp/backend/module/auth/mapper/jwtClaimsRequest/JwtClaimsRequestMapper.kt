package io.limberapp.backend.module.auth.mapper.jwtClaimsRequest

import com.google.inject.Inject
import io.limberapp.backend.module.auth.model.jwtClaimsRequest.JwtClaimsRequestModel
import io.limberapp.backend.module.auth.rep.jwtClaimsRequest.JwtClaimsRequestRep

internal class JwtClaimsRequestMapper @Inject constructor() {
  fun model(rep: JwtClaimsRequestRep.Creation) = JwtClaimsRequestModel(
      auth0ClientId = rep.auth0ClientId,
      firstName = rep.firstName,
      lastName = rep.lastName,
      emailAddress = rep.emailAddress,
      profilePhotoUrl = rep.profilePhotoUrl
  )
}

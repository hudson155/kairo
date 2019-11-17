package io.limberapp.backend.module.auth.mapper.api.jwtClaimsRequest

import com.google.inject.Inject
import io.limberapp.backend.module.auth.model.jwtClaimsRequest.JwtClaimsRequestModel
import io.limberapp.backend.module.auth.rep.jwtClaimsRequest.JwtClaimsRequestRep

internal class JwtClaimsRequestMapper @Inject constructor() {

    fun creationModel(rep: JwtClaimsRequestRep.Creation) = JwtClaimsRequestModel.Creation(
        firstName = rep.firstName,
        lastName = rep.lastName,
        emailAddress = rep.emailAddress,
        profilePhotoUrl = rep.profilePhotoUrl
    )

    fun completeRep(model: JwtClaimsRequestModel.Complete) = JwtClaimsRequestRep.Complete(
        orgs = model.orgs,
        roles = model.roles,
        user = model.user
    )
}

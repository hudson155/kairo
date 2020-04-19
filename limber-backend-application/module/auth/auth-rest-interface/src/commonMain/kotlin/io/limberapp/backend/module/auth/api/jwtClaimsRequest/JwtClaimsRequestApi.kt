package io.limberapp.backend.module.auth.api.jwtClaimsRequest

import com.piperframework.restInterface.HttpMethod
import com.piperframework.restInterface.PiperEndpoint
import io.limberapp.backend.module.auth.rep.jwtClaimsRequest.JwtClaimsRequestRep

@Suppress("StringLiteralDuplication")
object JwtClaimsRequestApi {

    data class Post(val rep: JwtClaimsRequestRep.Creation?) : PiperEndpoint(
        httpMethod = HttpMethod.POST,
        path = "/jwt-claims-request",
        body = rep
    )
}

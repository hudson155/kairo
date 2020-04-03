package io.limberapp.backend.module.auth.rep.jwtClaimsRequest

import com.piperframework.rep.CreationRep
import com.piperframework.validation.RepValidation
import com.piperframework.validation.ifPresent
import com.piperframework.validator.Validator
import io.limberapp.backend.authorization.principal.Claims
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object JwtClaimsRequestRep {

    @Serializable
    data class Creation(
        val auth0ClientId: String,
        val firstName: String,
        val lastName: String,
        val emailAddress: String,
        val profilePhotoUrl: String? = null
    ) : CreationRep {
        override fun validate() = RepValidation {
            validate(Creation::auth0ClientId) { Validator.auth0ClientId(value) }
            validate(Creation::firstName) { Validator.humanName(value) }
            validate(Creation::lastName) { Validator.humanName(value) }
            validate(Creation::emailAddress) { Validator.emailAddress(value) }
            validate(Creation::profilePhotoUrl) { ifPresent { Validator.url(value) } }
        }
    }

    @Serializable
    data class Complete(
        @SerialName(Claims.org)
        val org: String?,
        @SerialName(Claims.roles)
        val roles: String,
        @SerialName(Claims.user)
        val user: String
    ) : Any()
}

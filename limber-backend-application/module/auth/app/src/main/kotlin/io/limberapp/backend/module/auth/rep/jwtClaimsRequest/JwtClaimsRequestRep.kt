package io.limberapp.backend.module.auth.rep.jwtClaimsRequest

import com.fasterxml.jackson.annotation.JsonProperty
import io.limberapp.backend.authorization.principal.Claims
import io.limberapp.framework.rep.CreationRep
import io.limberapp.framework.validation.util.emailAddress
import io.limberapp.framework.validation.util.ifPresent
import io.limberapp.framework.validation.util.shortText
import io.limberapp.framework.validation.util.url

object JwtClaimsRequestRep {

    data class Creation(
        val firstName: String?,
        val lastName: String?,
        val emailAddress: String,
        val profilePhotoUrl: String?
    ) : CreationRep() {
        override fun validate() {
            validate(Creation::firstName) { ifPresent { shortText(allowEmpty = false) } }
            validate(Creation::lastName) { ifPresent { shortText(allowEmpty = false) } }
            validate(Creation::emailAddress) { emailAddress() }
            validate(Creation::profilePhotoUrl) { ifPresent { url() } }
        }
    }

    data class Complete(
        @JsonProperty(Claims.orgs)
        val orgs: String,
        @JsonProperty(Claims.roles)
        val roles: String,
        @JsonProperty(Claims.user)
        val user: String
    ) : Any()
}

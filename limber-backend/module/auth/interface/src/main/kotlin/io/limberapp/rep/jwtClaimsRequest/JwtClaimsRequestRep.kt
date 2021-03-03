package io.limberapp.rep.jwtClaimsRequest

import io.limberapp.rep.CreationRep
import io.limberapp.validation.RepValidation
import io.limberapp.validation.Validator
import io.limberapp.validation.ifPresent

object JwtClaimsRequestRep {
  data class Creation(
      val auth0ClientId: String,
      val firstName: String,
      val lastName: String,
      val emailAddress: String,
      val profilePhotoUrl: String? = null,
  ) : CreationRep {
    override fun validate(): RepValidation = RepValidation {
      validate(Creation::auth0ClientId) { Validator.auth0ClientId(this) }
      validate(Creation::firstName) { ifPresent { Validator.humanName(this) } }
      validate(Creation::lastName) { ifPresent { Validator.humanName(this) } }
      validate(Creation::emailAddress) { Validator.emailAddress(this) }
      validate(Creation::profilePhotoUrl) { ifPresent { Validator.url(this) } }
    }
  }
}

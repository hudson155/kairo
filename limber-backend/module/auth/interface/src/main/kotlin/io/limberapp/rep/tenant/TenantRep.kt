package io.limberapp.rep.tenant

import io.limberapp.rep.CompleteRep
import io.limberapp.rep.CreationRep
import io.limberapp.rep.UpdateRep
import io.limberapp.validation.RepValidation
import io.limberapp.validation.Validator
import io.limberapp.validation.ifPresent
import java.util.UUID

object TenantRep {
  data class Creation(
      val orgGuid: UUID,
      val auth0ClientId: String,
  ) : CreationRep {
    override fun validate(): RepValidation = RepValidation {
      validate(Creation::auth0ClientId) { Validator.auth0ClientId(this) }
    }
  }

  data class Complete(
      val orgGuid: UUID,
      val auth0ClientId: String,
      val domains: Set<TenantDomainRep.Complete>,
  ) : CompleteRep

  data class Update(
      val auth0ClientId: String? = null,
  ) : UpdateRep {
    override fun validate(): RepValidation = RepValidation {
      validate(Update::auth0ClientId) { ifPresent { Validator.auth0ClientId(this) } }
    }
  }
}

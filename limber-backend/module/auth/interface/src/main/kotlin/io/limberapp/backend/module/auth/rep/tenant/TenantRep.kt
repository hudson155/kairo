package io.limberapp.backend.module.auth.rep.tenant

import io.limberapp.common.validator.Validator
import io.limberapp.rep.CompleteRep
import io.limberapp.rep.CreationRep
import io.limberapp.rep.UpdateRep
import io.limberapp.validation.RepValidation
import io.limberapp.validation.ifPresent
import java.time.LocalDateTime
import java.util.*

object TenantRep {
  data class Creation(
      val orgGuid: UUID,
      val name: String,
      val auth0ClientId: String,
      val domain: TenantDomainRep.Creation,
  ) : CreationRep {
    override fun validate() = RepValidation {
      validate(Creation::name) { Validator.orgName(value) }
      validate(Creation::auth0ClientId) { Validator.auth0ClientId(value) }
      validate(Creation::domain)
    }
  }

  data class Complete(
      override val createdDate: LocalDateTime,
      val orgGuid: UUID,
      val name: String,
      val auth0ClientId: String,
      val domains: Set<TenantDomainRep.Complete>,
  ) : CompleteRep

  data class Update(
      val name: String? = null,
      val auth0ClientId: String? = null,
  ) : UpdateRep {
    override fun validate() = RepValidation {
      validate(Update::name) { ifPresent { Validator.orgName(value) } }
      validate(Update::auth0ClientId) { ifPresent { Validator.auth0ClientId(value) } }
    }
  }
}

package io.limberapp.backend.module.auth.rep.tenant

import io.limberapp.common.rep.CompleteRep
import io.limberapp.common.rep.CreationRep
import io.limberapp.common.rep.UpdateRep
import io.limberapp.common.validation.RepValidation
import io.limberapp.common.validation.ifPresent
import io.limberapp.common.validator.Validator
import java.time.LocalDateTime
import java.util.*

object TenantRep {
  data class Creation(
    val orgGuid: UUID,
    val auth0ClientId: String,
    val domain: TenantDomainRep.Creation,
  ) : CreationRep {
    override fun validate() = RepValidation {
      validate(Creation::auth0ClientId) { Validator.auth0ClientId(value) }
      validate(Creation::domain)
    }
  }

  data class Complete(
    override val createdDate: LocalDateTime,
    val orgGuid: UUID,
    val auth0ClientId: String,
    val domains: Set<TenantDomainRep.Complete>,
  ) : CompleteRep

  data class Update(
    val auth0ClientId: String? = null,
  ) : UpdateRep {
    override fun validate() = RepValidation {
      validate(Update::auth0ClientId) { ifPresent { Validator.auth0ClientId(value) } }
    }
  }
}

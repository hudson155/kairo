package io.limberapp.rep.tenant

import io.limberapp.rep.CompleteRep
import io.limberapp.rep.CreationRep
import io.limberapp.validation.RepValidation
import io.limberapp.validation.Validator

object TenantDomainRep {
  data class Creation(
      val domain: String,
  ) : CreationRep {
    override fun validate(): RepValidation = RepValidation {
      validate(Creation::domain) { Validator.hostname(this) }
    }
  }

  data class Complete(
      val domain: String,
  ) : CompleteRep
}

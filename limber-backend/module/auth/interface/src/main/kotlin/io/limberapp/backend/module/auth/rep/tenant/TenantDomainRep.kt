package io.limberapp.backend.module.auth.rep.tenant

import io.limberapp.common.validation.RepValidation
import io.limberapp.common.validation.Validator
import io.limberapp.rep.CompleteRep
import io.limberapp.rep.CreationRep
import java.time.ZonedDateTime

object TenantDomainRep {
  data class Creation(
      val domain: String,
  ) : CreationRep {
    override fun validate() = RepValidation {
      validate(Creation::domain) { Validator.hostname(value) }
    }
  }

  data class Complete(
      override val createdDate: ZonedDateTime,
      val domain: String,
  ) : CompleteRep
}

package io.limberapp.backend.module.auth.rep.tenant

import io.limberapp.common.rep.CompleteRep
import io.limberapp.common.rep.CreationRep
import io.limberapp.common.types.LocalDateTime
import io.limberapp.common.validation.RepValidation
import io.limberapp.common.validator.Validator

object TenantDomainRep {
  data class Creation(
    val domain: String,
  ) : CreationRep {
    override fun validate() = RepValidation {
      validate(Creation::domain) { Validator.hostname(value) }
    }
  }

  data class Complete(
    override val createdDate: LocalDateTime,
    val domain: String,
  ) : CompleteRep
}

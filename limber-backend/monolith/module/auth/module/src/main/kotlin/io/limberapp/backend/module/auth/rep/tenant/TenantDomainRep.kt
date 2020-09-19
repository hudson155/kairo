package io.limberapp.backend.module.auth.rep.tenant

import io.limberapp.rep.CompleteRep
import io.limberapp.rep.CreationRep
import io.limberapp.validation.RepValidation
import io.limberapp.common.validator.Validator
import java.time.LocalDateTime

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

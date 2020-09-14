package io.limberapp.backend.module.auth.rep.tenant

import io.limberapp.common.rep.CompleteRep
import io.limberapp.common.rep.CreationRep
import io.limberapp.common.serialization.serializer.LocalDateTimeSerializer
import io.limberapp.common.types.LocalDateTime
import io.limberapp.common.validation.RepValidation
import io.limberapp.common.validator.Validator
import kotlinx.serialization.Serializable

object TenantDomainRep {
  @Serializable
  data class Creation(
    val domain: String,
  ) : CreationRep {
    override fun validate() = RepValidation {
      validate(Creation::domain) { Validator.hostname(value) }
    }
  }

  @Serializable
  data class Complete(
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdDate: LocalDateTime,
    val domain: String,
  ) : CompleteRep
}

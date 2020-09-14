package io.limberapp.backend.module.auth.rep.tenant

import io.limberapp.common.rep.CompleteRep
import io.limberapp.common.rep.CreationRep
import io.limberapp.common.rep.UpdateRep
import io.limberapp.common.serialization.serializer.LocalDateTimeSerializer
import io.limberapp.common.serialization.serializer.UuidSerializer
import io.limberapp.common.types.LocalDateTime
import io.limberapp.common.types.UUID
import io.limberapp.common.validation.RepValidation
import io.limberapp.common.validation.ifPresent
import io.limberapp.common.validator.Validator
import kotlinx.serialization.Serializable

object TenantRep {
  @Serializable
  data class Creation(
    @Serializable(with = UuidSerializer::class)
    val orgGuid: UUID,
    val auth0ClientId: String,
    val domain: TenantDomainRep.Creation,
  ) : CreationRep {
    override fun validate() = RepValidation {
      validate(Creation::auth0ClientId) { Validator.auth0ClientId(value) }
      validate(Creation::domain)
    }
  }

  @Serializable
  data class Complete(
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdDate: LocalDateTime,
    @Serializable(with = UuidSerializer::class)
    val orgGuid: UUID,
    val auth0ClientId: String,
    val domains: Set<TenantDomainRep.Complete>,
  ) : CompleteRep

  @Serializable
  data class Update(
    val auth0ClientId: String? = null,
  ) : UpdateRep {
    override fun validate() = RepValidation {
      validate(Update::auth0ClientId) { ifPresent { Validator.auth0ClientId(value) } }
    }
  }
}

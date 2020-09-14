package io.limberapp.backend.module.orgs.rep.org

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

object OrgRep {
  @Serializable
  data class Creation(
    val name: String,
  ) : CreationRep {
    override fun validate() = RepValidation {
      validate(Creation::name) { Validator.orgName(value) }
    }
  }

  @Serializable
  data class Complete(
    @Serializable(with = UuidSerializer::class)
    val guid: UUID,
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdDate: LocalDateTime,
    val name: String,
    @Serializable(with = UuidSerializer::class)
    val ownerUserGuid: UUID?,
    val features: List<FeatureRep.Complete>,
  ) : CompleteRep

  @Serializable
  data class Update(
    val name: String? = null,
    @Serializable(with = UuidSerializer::class)
    val ownerUserGuid: UUID? = null,
  ) : UpdateRep {
    override fun validate() = RepValidation {
      validate(Update::name) { ifPresent { Validator.orgName(value) } }
    }
  }
}

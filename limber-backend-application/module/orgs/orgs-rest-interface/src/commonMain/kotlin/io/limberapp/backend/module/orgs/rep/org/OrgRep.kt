package io.limberapp.backend.module.orgs.rep.org

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import com.piperframework.rep.UpdateRep
import com.piperframework.serialization.serializer.LocalDateTimeSerializer
import com.piperframework.serialization.serializer.UuidSerializer
import com.piperframework.types.LocalDateTime
import com.piperframework.types.UUID
import com.piperframework.validation.RepValidation
import com.piperframework.validation.ifPresent
import com.piperframework.validator.Validator
import kotlinx.serialization.Serializable

object OrgRep {
  @Serializable
  data class Creation(
    val name: String,
    @Serializable(with = UuidSerializer::class)
    val ownerAccountGuid: UUID?,
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
    val ownerAccountGuid: UUID?,
    val features: List<FeatureRep.Complete>,
  ) : CompleteRep

  @Serializable
  data class Update(
    val name: String? = null,
  ) : UpdateRep {
    override fun validate() = RepValidation {
      validate(Update::name) { ifPresent { Validator.orgName(value) } }
    }
  }
}

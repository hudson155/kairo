package io.limberapp.backend.module.auth.rep.org

import io.limberapp.common.rep.CompleteRep
import io.limberapp.common.rep.CreationRep
import io.limberapp.common.serialization.serializer.LocalDateTimeSerializer
import io.limberapp.common.serialization.serializer.UuidSerializer
import io.limberapp.common.types.LocalDateTime
import io.limberapp.common.types.UUID
import io.limberapp.common.validation.RepValidation
import kotlinx.serialization.Serializable

object OrgRoleMembershipRep {
  @Serializable
  data class Creation(
    @Serializable(with = UuidSerializer::class)
    val accountGuid: UUID,
  ) : CreationRep {
    override fun validate() = RepValidation {}
  }

  @Serializable
  data class Complete(
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdDate: LocalDateTime,
    @Serializable(with = UuidSerializer::class)
    val accountGuid: UUID,
  ) : CompleteRep
}

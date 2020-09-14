package io.limberapp.backend.module.auth.rep.org

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import com.piperframework.serialization.serializer.LocalDateTimeSerializer
import com.piperframework.serialization.serializer.UuidSerializer
import com.piperframework.types.LocalDateTime
import com.piperframework.types.UUID
import com.piperframework.validation.RepValidation
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

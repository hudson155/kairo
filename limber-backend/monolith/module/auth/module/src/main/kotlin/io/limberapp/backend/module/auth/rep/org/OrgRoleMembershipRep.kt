package io.limberapp.backend.module.auth.rep.org

import io.limberapp.common.rep.CompleteRep
import io.limberapp.common.rep.CreationRep
import io.limberapp.common.types.LocalDateTime
import io.limberapp.common.types.UUID
import io.limberapp.common.validation.RepValidation

object OrgRoleMembershipRep {
  data class Creation(
    val accountGuid: UUID,
  ) : CreationRep {
    override fun validate() = RepValidation {}
  }

  data class Complete(
    override val createdDate: LocalDateTime,
    val accountGuid: UUID,
  ) : CompleteRep
}

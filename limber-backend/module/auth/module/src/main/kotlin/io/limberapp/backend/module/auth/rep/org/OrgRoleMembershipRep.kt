package io.limberapp.backend.module.auth.rep.org

import io.limberapp.rep.CompleteRep
import io.limberapp.rep.CreationRep
import io.limberapp.validation.RepValidation
import java.time.LocalDateTime
import java.util.*

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

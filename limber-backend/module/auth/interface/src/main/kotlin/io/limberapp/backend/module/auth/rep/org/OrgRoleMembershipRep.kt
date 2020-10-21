package io.limberapp.backend.module.auth.rep.org

import io.limberapp.common.rep.CompleteRep
import io.limberapp.common.rep.CreationRep
import io.limberapp.common.validation.RepValidation
import java.time.ZonedDateTime
import java.util.*

object OrgRoleMembershipRep {
  data class Creation(
      val accountGuid: UUID,
  ) : CreationRep {
    override fun validate() = RepValidation {}
  }

  data class Complete(
      override val createdDate: ZonedDateTime,
      val accountGuid: UUID,
  ) : CompleteRep
}

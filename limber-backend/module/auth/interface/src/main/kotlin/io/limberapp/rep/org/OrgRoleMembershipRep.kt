package io.limberapp.rep.org

import io.limberapp.rep.CompleteRep
import io.limberapp.rep.CreationRep
import io.limberapp.validation.RepValidation
import java.util.UUID

object OrgRoleMembershipRep {
  data class Creation(
      val userGuid: UUID,
  ) : CreationRep {
    override fun validate(): RepValidation = RepValidation {}
  }

  data class Complete(
      val userGuid: UUID,
  ) : CompleteRep
}

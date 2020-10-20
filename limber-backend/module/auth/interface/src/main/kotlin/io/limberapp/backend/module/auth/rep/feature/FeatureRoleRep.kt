package io.limberapp.backend.module.auth.rep.feature

import io.limberapp.common.validation.RepValidation
import io.limberapp.permissions.featurePermissions.FeaturePermissions
import io.limberapp.rep.CompleteRep
import io.limberapp.rep.CreationRep
import io.limberapp.rep.UpdateRep
import java.time.LocalDateTime
import java.util.*

object FeatureRoleRep {
  data class Creation(
      val orgRoleGuid: UUID,
      val permissions: FeaturePermissions,
  ) : CreationRep {
    override fun validate() = RepValidation {}
  }

  data class Complete(
      val guid: UUID,
      override val createdDate: LocalDateTime,
      val orgRoleGuid: UUID,
      val permissions: FeaturePermissions,
  ) : CompleteRep

  data class Update(
      val permissions: FeaturePermissions? = null,
  ) : UpdateRep {
    override fun validate() = RepValidation {}
  }
}

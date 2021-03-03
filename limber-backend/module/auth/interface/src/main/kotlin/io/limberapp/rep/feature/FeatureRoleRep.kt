package io.limberapp.rep.feature

import io.limberapp.permissions.feature.FeaturePermissions
import io.limberapp.rep.CompleteRep
import io.limberapp.rep.CreationRep
import io.limberapp.rep.UpdateRep
import io.limberapp.validation.RepValidation
import java.util.UUID

object FeatureRoleRep {
  data class Creation(
      val featureGuid: UUID,
      val orgRoleGuid: UUID,
      val permissions: FeaturePermissions,
  ) : CreationRep {
    override fun validate(): RepValidation = RepValidation {}
  }

  data class Complete(
      val guid: UUID,
      val featureGuid: UUID,
      val orgRoleGuid: UUID,
      val permissions: FeaturePermissions,
  ) : CompleteRep

  data class Update(
      val permissions: FeaturePermissions? = null,
  ) : UpdateRep {
    override fun validate(): RepValidation = RepValidation {}
  }
}

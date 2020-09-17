package io.limberapp.backend.module.auth.rep.feature

import io.limberapp.backend.authorization.permissions.featurePermissions.FeaturePermissions
import io.limberapp.common.rep.CompleteRep
import io.limberapp.common.rep.CreationRep
import io.limberapp.common.rep.UpdateRep
import io.limberapp.common.validation.RepValidation
import java.time.LocalDateTime
import java.util.*

object FeatureRoleRep {
  data class Creation(
    val orgRoleGuid: UUID,
    val permissions: FeaturePermissions,
    val isDefault: Boolean,
  ) : CreationRep {
    override fun validate() = RepValidation {}
  }

  data class Complete(
    val guid: UUID,
    override val createdDate: LocalDateTime,
    val orgRoleGuid: UUID,
    val permissions: FeaturePermissions,
    val isDefault: Boolean,
  ) : CompleteRep

  data class Update(
    val permissions: FeaturePermissions? = null,
    val isDefault: Boolean? = null,
  ) : UpdateRep {
    override fun validate() = RepValidation {}
  }
}

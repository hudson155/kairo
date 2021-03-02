package io.limberapp.rep.org

import io.limberapp.permissions.org.OrgPermissions
import io.limberapp.rep.CompleteRep
import io.limberapp.rep.CreationRep
import io.limberapp.rep.UpdateRep
import io.limberapp.validation.RepValidation
import io.limberapp.validation.Validator
import io.limberapp.validation.ifPresent
import java.util.UUID

object OrgRoleRep {
  data class Creation(
      val orgGuid: UUID,
      val name: String,
      val permissions: OrgPermissions,
      val isDefault: Boolean,
  ) : CreationRep {
    override fun validate(): RepValidation = RepValidation {
      validate(Creation::name) { Validator.orgRoleName(this) }
    }
  }

  data class Complete(
      val guid: UUID,
      val orgGuid: UUID,
      val name: String,
      val slug: String,
      val permissions: OrgPermissions,
      val isDefault: Boolean,
      val memberCount: Int,
  ) : CompleteRep

  data class Update(
      val name: String? = null,
      val permissions: OrgPermissions? = null,
      val isDefault: Boolean? = null,
  ) : UpdateRep {
    override fun validate(): RepValidation = RepValidation {
      validate(Update::name) { ifPresent { Validator.featureName(this) } }
    }
  }
}

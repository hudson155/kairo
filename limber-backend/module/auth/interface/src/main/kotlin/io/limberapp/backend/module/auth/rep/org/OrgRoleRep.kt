package io.limberapp.backend.module.auth.rep.org

import io.limberapp.common.validator.Validator
import io.limberapp.permissions.orgPermissions.OrgPermissions
import io.limberapp.rep.CompleteRep
import io.limberapp.rep.CreationRep
import io.limberapp.rep.UpdateRep
import io.limberapp.util.url.slugify
import io.limberapp.validation.RepValidation
import io.limberapp.validation.ifPresent
import java.time.LocalDateTime
import java.util.*

object OrgRoleRep {
  data class Creation(
      val name: String,
      val permissions: OrgPermissions,
      val isDefault: Boolean,
  ) : CreationRep {
    override fun validate() = RepValidation {
      validate(Creation::name) { Validator.orgRoleName(value) }
    }
  }

  data class Complete(
      val guid: UUID,
      override val createdDate: LocalDateTime,
      val name: String,
      val permissions: OrgPermissions,
      val isDefault: Boolean,
      val memberCount: Int,
  ) : CompleteRep {
    val slug get() = name.slugify()
  }

  data class Update(
      val name: String? = null,
      val permissions: OrgPermissions? = null,
      val isDefault: Boolean? = null,
  ) : UpdateRep {
    override fun validate() = RepValidation {
      validate(Update::name) { ifPresent { Validator.featureName(value) } }
    }
  }
}

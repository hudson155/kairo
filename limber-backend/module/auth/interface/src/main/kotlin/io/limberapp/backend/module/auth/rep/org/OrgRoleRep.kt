package io.limberapp.backend.module.auth.rep.org

import io.limberapp.common.util.url.slugify
import io.limberapp.common.validation.RepValidation
import io.limberapp.common.validation.Validator
import io.limberapp.common.validation.ifPresent
import io.limberapp.permissions.orgPermissions.OrgPermissions
import io.limberapp.rep.CompleteRep
import io.limberapp.rep.CreationRep
import io.limberapp.rep.UpdateRep
import java.time.ZonedDateTime
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
      override val createdDate: ZonedDateTime,
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

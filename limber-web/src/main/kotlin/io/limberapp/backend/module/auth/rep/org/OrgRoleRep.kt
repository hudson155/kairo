package io.limberapp.backend.module.auth.rep.org

import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermissions
import io.limberapp.common.rep.CompleteRep
import io.limberapp.common.rep.CreationRep
import io.limberapp.common.rep.UpdateRep
import io.limberapp.common.serialization.serializer.LocalDateTimeSerializer
import io.limberapp.common.serialization.serializer.UuidSerializer
import io.limberapp.common.types.LocalDateTime
import io.limberapp.common.util.url.slugify
import io.limberapp.common.validation.RepValidation
import io.limberapp.common.validation.ifPresent
import io.limberapp.common.validator.Validator
import kotlinx.serialization.Serializable

object OrgRoleRep {
  @Serializable
  data class Creation(
    val name: String,
    val permissions: OrgPermissions,
    val isDefault: Boolean,
  ) : CreationRep {
    override fun validate() = RepValidation {
      validate(Creation::name) { Validator.orgRoleName(value) }
    }
  }

  @Serializable
  data class Complete(
    @Serializable(with = UuidSerializer::class)
    val guid: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdDate: LocalDateTime,
    val name: String,
    val permissions: OrgPermissions,
    val isDefault: Boolean,
    val memberCount: Int,
  ) : CompleteRep {
    val slug get() = name.slugify()
  }

  @Serializable
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

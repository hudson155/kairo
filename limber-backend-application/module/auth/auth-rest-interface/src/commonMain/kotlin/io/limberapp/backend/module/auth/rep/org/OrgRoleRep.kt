package io.limberapp.backend.module.auth.rep.org

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import com.piperframework.rep.UpdateRep
import com.piperframework.serialization.serializer.LocalDateTimeSerializer
import com.piperframework.serialization.serializer.UuidSerializer
import com.piperframework.types.LocalDateTime
import com.piperframework.types.UUID
import com.piperframework.util.slugify
import com.piperframework.validation.RepValidation
import com.piperframework.validation.ifPresent
import com.piperframework.validator.Validator
import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermissions
import kotlinx.serialization.Serializable

object OrgRoleRep {
  @Serializable
  data class Creation(
    val name: String,
    val permissions: OrgPermissions
  ) : CreationRep {
    override fun validate() = RepValidation {
      validate(Creation::name) { Validator.orgRoleName(value) }
    }
  }

  @Serializable
  data class Complete(
    @Serializable(with = UuidSerializer::class)
    val guid: UUID,
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdDate: LocalDateTime,
    val name: String,
    val permissions: OrgPermissions,
    val memberCount: Int
  ) : CompleteRep {
    val slug get() = name.slugify()
    val uniqueSortKey get() = "$createdDate-$guid"
  }

  @Serializable
  data class Update(
    val name: String? = null,
    val permissions: OrgPermissions? = null
  ) : UpdateRep {
    override fun validate() = RepValidation {
      validate(Update::name) { ifPresent { Validator.featureName(value) } }
    }
  }
}

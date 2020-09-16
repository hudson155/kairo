package io.limberapp.backend.module.auth.rep.feature

import io.limberapp.backend.authorization.permissions.featurePermissions.FeaturePermissions
import io.limberapp.common.rep.CompleteRep
import io.limberapp.common.rep.CreationRep
import io.limberapp.common.rep.UpdateRep
import io.limberapp.common.serialization.serializer.LocalDateTimeSerializer
import io.limberapp.common.serialization.serializer.UuidSerializer
import io.limberapp.common.types.LocalDateTime
import io.limberapp.common.validation.RepValidation
import kotlinx.serialization.Serializable

object FeatureRoleRep {
  @Serializable
  data class Creation(
    @Serializable(with = UuidSerializer::class)
    val orgRoleGuid: String,
    val permissions: FeaturePermissions,
    val isDefault: Boolean,
  ) : CreationRep {
    override fun validate() = RepValidation {}
  }

  @Serializable
  data class Complete(
    @Serializable(with = UuidSerializer::class)
    val guid: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdDate: LocalDateTime,
    @Serializable(with = UuidSerializer::class)
    val orgRoleGuid: String,
    val permissions: FeaturePermissions,
    val isDefault: Boolean,
  ) : CompleteRep

  @Serializable
  data class Update(
    val permissions: FeaturePermissions? = null,
    val isDefault: Boolean? = null,
  ) : UpdateRep {
    override fun validate() = RepValidation {}
  }
}

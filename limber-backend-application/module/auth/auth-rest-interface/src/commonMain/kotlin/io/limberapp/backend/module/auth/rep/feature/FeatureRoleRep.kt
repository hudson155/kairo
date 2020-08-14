package io.limberapp.backend.module.auth.rep.feature

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import com.piperframework.rep.UpdateRep
import com.piperframework.serialization.serializer.LocalDateTimeSerializer
import com.piperframework.serialization.serializer.UuidSerializer
import com.piperframework.types.LocalDateTime
import com.piperframework.types.UUID
import com.piperframework.validation.RepValidation
import io.limberapp.backend.authorization.permissions.featurePermissions.FeaturePermissions
import kotlinx.serialization.Serializable

object FeatureRoleRep {
  @Serializable
  data class Creation(
    @Serializable(with = UuidSerializer::class)
    val orgRoleGuid: UUID,
    val permissions: FeaturePermissions,
  ) : CreationRep {
    override fun validate() = RepValidation {}
  }

  @Serializable
  data class Complete(
    @Serializable(with = UuidSerializer::class)
    val guid: UUID,
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdDate: LocalDateTime,
    @Serializable(with = UuidSerializer::class)
    val orgRoleGuid: UUID,
    val permissions: FeaturePermissions,
  ) : CompleteRep {
    val uniqueSortKey get() = "$createdDate-$guid"
  }

  @Serializable
  data class Update(
    val permissions: FeaturePermissions? = null,
  ) : UpdateRep {
    override fun validate() = RepValidation {}
  }
}

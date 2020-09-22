package io.limberapp.backend.module.auth.model.feature

import io.limberapp.backend.authorization.permissions.featurePermissions.FeaturePermissions
import java.time.LocalDateTime
import java.util.*

data class FeatureRoleModel(
  val guid: UUID,
  val createdDate: LocalDateTime,
  val featureGuid: UUID,
  val orgRoleGuid: UUID,
  val permissions: FeaturePermissions,
) {
  data class Update(
    val permissions: FeaturePermissions?,
  )
}

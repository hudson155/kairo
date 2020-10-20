package io.limberapp.backend.module.auth.model.feature

import io.limberapp.permissions.featurePermissions.FeaturePermissions
import java.time.ZonedDateTime
import java.util.*

data class FeatureRoleModel(
    val guid: UUID,
    val createdDate: ZonedDateTime,
    val featureGuid: UUID,
    val orgRoleGuid: UUID,
    val permissions: FeaturePermissions,
) {
  data class Update(
      val permissions: FeaturePermissions?,
  )
}

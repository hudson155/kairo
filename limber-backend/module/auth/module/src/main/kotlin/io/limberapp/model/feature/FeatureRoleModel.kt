package io.limberapp.model.feature

import io.limberapp.permissions.feature.FeaturePermissions
import java.time.ZonedDateTime
import java.util.UUID

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

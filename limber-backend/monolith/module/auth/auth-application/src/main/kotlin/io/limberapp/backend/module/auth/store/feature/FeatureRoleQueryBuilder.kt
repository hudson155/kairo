package io.limberapp.backend.module.auth.store.feature

import io.limberapp.backend.module.auth.model.feature.FeatureRoleFinder
import io.limberapp.common.store.QueryBuilder
import java.util.*

internal class FeatureRoleQueryBuilder : QueryBuilder(), FeatureRoleFinder {
  override fun featureGuid(featureGuid: UUID) {
    conditions += "feature_guid = :featureGuid"
    bindings["featureGuid"] = featureGuid
  }

  override fun featureRoleGuid(featureRoleGuid: UUID) {
    conditions += "guid = :featureRoleGuid"
    bindings["featureRoleGuid"] = featureRoleGuid
  }
}

package io.limberapp.backend.module.orgs.store.feature

import com.piperframework.store.QueryBuilder
import io.limberapp.backend.module.orgs.model.org.FeatureFinder
import java.util.*

internal class FeatureQueryBuilder : QueryBuilder(), FeatureFinder {
  override fun featureGuid(featureGuid: UUID) {
    conditions += "guid = :featureGuid"
    bindings["featureGuid"] = featureGuid
  }

  override fun orgGuid(orgGuid: UUID) {
    conditions += "org_guid = :orgGuid"
    bindings["orgGuid"] = orgGuid
  }
}

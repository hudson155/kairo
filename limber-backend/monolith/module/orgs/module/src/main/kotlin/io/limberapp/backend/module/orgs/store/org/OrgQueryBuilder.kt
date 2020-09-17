package io.limberapp.backend.module.orgs.store.org

import io.limberapp.backend.module.orgs.model.org.OrgFinder
import io.limberapp.common.store.QueryBuilder
import java.util.*

internal class OrgQueryBuilder : QueryBuilder(), OrgFinder {
  override fun orgGuid(orgGuid: UUID) {
    conditions += "guid = :orgGuid"
    bindings["orgGuid"] = orgGuid
  }

  override fun ownerUserGuid(ownerUserGuid: UUID) {
    conditions += "owner_user_guid = :ownerUserGuid"
    bindings["ownerUserGuid"] = ownerUserGuid
  }
}

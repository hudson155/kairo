package io.limberapp.backend.module.orgs.store.org

import com.piperframework.store.QueryBuilder
import io.limberapp.backend.module.orgs.model.org.OrgFinder
import java.util.*

internal class OrgQueryBuilder : QueryBuilder(), OrgFinder {
  override fun orgGuid(orgGuid: UUID) {
    conditions += "guid = :orgGuid"
    bindings["orgGuid"] = orgGuid
  }

  override fun ownerAccountGuid(ownerAccountGuid: UUID) {
    conditions += "owner_account_guid = :ownerAccountGuid"
    bindings["ownerAccountGuid"] = ownerAccountGuid
  }
}

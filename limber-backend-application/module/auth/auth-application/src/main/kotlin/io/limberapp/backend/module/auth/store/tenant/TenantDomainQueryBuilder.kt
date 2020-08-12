package io.limberapp.backend.module.auth.store.tenant

import com.piperframework.store.QueryBuilder
import io.limberapp.backend.module.auth.model.tenant.TenantDomainFinder
import java.util.*

internal class TenantDomainQueryBuilder : QueryBuilder(), TenantDomainFinder {
  override fun orgGuid(orgGuid: UUID) {
    conditions += "org_guid = :orgGuid"
    bindings["orgGuid"] = orgGuid
  }

  override fun domain(domain: String) {
    conditions += "LOWER(domain) = LOWER(:domain)"
    bindings["domain"] = domain
  }
}

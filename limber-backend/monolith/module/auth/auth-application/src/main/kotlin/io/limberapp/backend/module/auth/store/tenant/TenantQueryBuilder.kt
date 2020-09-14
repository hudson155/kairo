package io.limberapp.backend.module.auth.store.tenant

import io.limberapp.backend.module.auth.model.tenant.TenantFinder
import io.limberapp.common.store.QueryBuilder
import java.util.*

internal class TenantQueryBuilder : QueryBuilder(), TenantFinder {
  override fun orgGuid(orgGuid: UUID) {
    conditions += "org_guid = :orgGuid"
    bindings["orgGuid"] = orgGuid
  }

  override fun auth0ClientId(auth0ClientId: String) {
    conditions += "auth0_client_id = :auth0ClientId"
    bindings["auth0ClientId"] = auth0ClientId
  }

  override fun domain(domain: String) {
    conditions +=
      """
      EXISTS(SELECT 1
             FROM auth.tenant_domain
             WHERE tenant_domain.org_guid = tenant.org_guid
               AND LOWER(domain) = LOWER(:domain))
      """.trimIndent()
    bindings["domain"] = domain
  }
}

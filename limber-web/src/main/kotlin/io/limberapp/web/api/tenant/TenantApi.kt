package io.limberapp.web.api.tenant

import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.web.api.Fetch

internal class TenantApi(private val fetch: Fetch) {

    suspend fun get(tenantDomain: String): TenantRep.Complete {
        return fetch.get("/tenants", queryParams = listOf("domain" to tenantDomain))
            .unsafeCast<TenantRep.Complete>()
    }
}

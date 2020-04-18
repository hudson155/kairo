package io.limberapp.web.api.tenant

import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.web.api.Fetch
import io.limberapp.web.api.json

internal class TenantApi(private val fetch: Fetch) {

    suspend fun get(tenantDomain: String): TenantRep.Complete {
        val string = fetch.get("/tenants", queryParams = listOf("domain" to tenantDomain))
        return json.parse(string)
    }
}

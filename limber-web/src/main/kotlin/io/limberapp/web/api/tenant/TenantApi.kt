package io.limberapp.web.api.tenant

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.auth.rep.tenant.TenantRep

internal class TenantApi(private val fetch: Fetch, private val json: Json) {

    suspend fun get(tenantDomain: String): TenantRep.Complete {
        val string = fetch.get("/tenants", queryParams = listOf("domain" to tenantDomain))
        return json.parse(string)
    }
}

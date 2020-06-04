package io.limberapp.backend.module.auth.client.tenant

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.rep.tenant.TenantRep

class TenantClient(private val fetch: Fetch, private val json: Json) {
  suspend operator fun invoke(endpoint: TenantApi.GetByDomain) =
    fetch(endpoint) { json.parse<TenantRep.Complete>(it) }
}

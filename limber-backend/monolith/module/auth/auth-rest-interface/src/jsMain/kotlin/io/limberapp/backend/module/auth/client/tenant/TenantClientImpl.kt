package io.limberapp.backend.module.auth.client.tenant

import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.common.restInterface.Fetch
import io.limberapp.common.serialization.Json

class TenantClientImpl(private val fetch: Fetch, private val json: Json) : TenantClient {
  override suspend operator fun invoke(endpoint: TenantApi.GetByDomain) =
    fetch(endpoint) { json.parse<TenantRep.Complete>(it) }
}

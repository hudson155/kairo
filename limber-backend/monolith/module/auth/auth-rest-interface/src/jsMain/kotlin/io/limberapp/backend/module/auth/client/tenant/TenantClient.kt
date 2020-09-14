package io.limberapp.backend.module.auth.client.tenant

import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.common.util.Outcome

interface TenantClient {
  suspend operator fun invoke(endpoint: TenantApi.GetByDomain): Outcome<TenantRep.Complete>
}

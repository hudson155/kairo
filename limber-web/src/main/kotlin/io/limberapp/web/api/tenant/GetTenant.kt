package io.limberapp.web.api.tenant

import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.web.api.Api

internal suspend fun getTenant(tenantDomain: String): TenantRep.Complete {
    return Api.get("/tenants/$tenantDomain")
        .unsafeCast<TenantRep.Complete>()
}

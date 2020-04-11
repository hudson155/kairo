package api.tenant

import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import kotlinx.coroutines.await
import rootUrl
import util.encodeURIComponent
import kotlin.browser.window

internal suspend fun getTenant(tenantDomain: String): TenantRep.Complete {
    return window.fetch("$rootUrl/tenants/${encodeURIComponent(tenantDomain)}").await()
        .json().await()
        .unsafeCast<TenantRep.Complete>()
}

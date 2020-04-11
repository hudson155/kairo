package io.limberapp.web.tenant

import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.web.rootUrl
import io.limberapp.web.util.encodeURIComponent
import kotlinx.coroutines.await
import kotlin.browser.window

internal suspend fun getTenant(tenantDomain: String): TenantRep.Complete {
    return window.fetch("$rootUrl/tenants/${encodeURIComponent(tenantDomain)}").await()
        .json().await()
        .unsafeCast<TenantRep.Complete>()
}

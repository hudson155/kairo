package io.limberapp.backend.module.auth.service.tenant

import com.google.inject.Inject
import io.limberapp.backend.module.auth.store.tenant.TenantStore

internal class TenantServiceImpl @Inject constructor(
    private val tenantStore: TenantStore
) : TenantService by tenantStore

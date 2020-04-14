package io.limberapp.backend.module.auth.service.tenant

import com.google.inject.Inject
import io.limberapp.backend.module.auth.store.tenant.TenantDomainStore

internal class TenantDomainServiceImpl @Inject constructor(
    private val tenantDomainStore: TenantDomainStore
) : TenantDomainService by tenantDomainStore

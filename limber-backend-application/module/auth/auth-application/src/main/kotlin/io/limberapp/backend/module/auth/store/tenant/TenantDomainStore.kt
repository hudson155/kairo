package io.limberapp.backend.module.auth.store.tenant

import io.limberapp.backend.module.auth.model.tenant.TenantDomainModel
import io.limberapp.backend.module.auth.service.tenant.TenantDomainService
import java.util.UUID

internal interface TenantDomainStore : TenantDomainService {
    fun create(orgId: UUID, models: Set<TenantDomainModel>)

    fun getByOrgId(orgId: UUID): Set<TenantDomainModel>
}

package io.limberapp.backend.module.auth.store.tenant

import io.limberapp.backend.module.auth.model.tenant.TenantDomainModel
import io.limberapp.backend.module.auth.service.tenant.TenantDomainService
import java.util.UUID

internal interface TenantDomainStore : TenantDomainService {
    fun create(orgGuid: UUID, models: Set<TenantDomainModel>)

    fun getByOrgGuid(orgGuid: UUID): Set<TenantDomainModel>
}

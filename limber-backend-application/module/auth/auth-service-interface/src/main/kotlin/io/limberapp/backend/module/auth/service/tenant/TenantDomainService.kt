package io.limberapp.backend.module.auth.service.tenant

import io.limberapp.backend.module.auth.model.tenant.TenantDomainModel
import java.util.UUID

interface TenantDomainService {
    fun create(orgId: UUID, model: TenantDomainModel)

    fun delete(orgId: UUID, domain: String)
}

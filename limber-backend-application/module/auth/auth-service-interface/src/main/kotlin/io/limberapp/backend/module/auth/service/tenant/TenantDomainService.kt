package io.limberapp.backend.module.auth.service.tenant

import io.limberapp.backend.module.auth.model.tenant.TenantDomainModel
import java.util.UUID

interface TenantDomainService {
    fun create(orgGuid: UUID, model: TenantDomainModel)

    fun delete(orgGuid: UUID, domain: String)
}

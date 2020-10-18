package io.limberapp.backend.module.auth.service.tenant

import io.limberapp.backend.module.auth.model.tenant.TenantDomainModel
import java.util.*

interface TenantDomainService {
  fun create(model: TenantDomainModel): TenantDomainModel

  fun getByOrgGuid(orgGuid: UUID): Set<TenantDomainModel>

  fun delete(orgGuid: UUID, domain: String)
}

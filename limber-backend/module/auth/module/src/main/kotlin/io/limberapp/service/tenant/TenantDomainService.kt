package io.limberapp.service.tenant

import io.limberapp.model.tenant.TenantDomainModel
import java.util.UUID

interface TenantDomainService {
  fun create(model: TenantDomainModel): TenantDomainModel

  fun getByOrgGuid(orgGuid: UUID): Set<TenantDomainModel>

  fun delete(orgGuid: UUID, domain: String)
}

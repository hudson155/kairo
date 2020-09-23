package io.limberapp.backend.module.auth.service.tenant

import io.limberapp.backend.LimberModule
import io.limberapp.backend.module.auth.model.tenant.TenantDomainFinder
import io.limberapp.backend.module.auth.model.tenant.TenantDomainModel
import io.limberapp.common.finder.Finder
import java.util.*

@LimberModule.Auth
interface TenantDomainService : Finder<TenantDomainModel, TenantDomainFinder> {
  fun create(model: TenantDomainModel): TenantDomainModel

  fun delete(orgGuid: UUID, domain: String)
}

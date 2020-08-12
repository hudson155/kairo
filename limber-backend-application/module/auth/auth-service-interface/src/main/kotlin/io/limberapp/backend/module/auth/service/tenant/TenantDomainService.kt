package io.limberapp.backend.module.auth.service.tenant

import com.piperframework.finder.Finder
import io.limberapp.backend.LimberModule
import io.limberapp.backend.module.auth.model.tenant.TenantDomainFinder
import io.limberapp.backend.module.auth.model.tenant.TenantDomainModel
import java.util.*

@LimberModule.Auth
interface TenantDomainService : Finder<TenantDomainModel, TenantDomainFinder> {
  fun create(model: TenantDomainModel): TenantDomainModel

  fun delete(orgGuid: UUID, domain: String)
}

package io.limberapp.backend.module.auth.service.tenant

import com.google.inject.Inject
import io.limberapp.backend.module.auth.model.tenant.TenantDomainFinder
import io.limberapp.backend.module.auth.model.tenant.TenantDomainModel
import io.limberapp.backend.module.auth.store.tenant.TenantDomainStore
import io.limberapp.common.finder.Finder
import java.util.*

internal class TenantDomainServiceImpl @Inject constructor(
    private val tenantDomainStore: TenantDomainStore,
) : TenantDomainService, Finder<TenantDomainModel, TenantDomainFinder> by tenantDomainStore {
  override fun create(model: TenantDomainModel) =
      tenantDomainStore.create(model)

  override fun delete(orgGuid: UUID, domain: String) =
      tenantDomainStore.delete(orgGuid, domain)
}

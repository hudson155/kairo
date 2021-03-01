package io.limberapp.service.tenant

import com.google.inject.Inject
import io.limberapp.model.tenant.TenantDomainModel
import io.limberapp.store.tenant.TenantDomainStore
import java.util.UUID

internal class TenantDomainServiceImpl @Inject constructor(
    private val tenantDomainStore: TenantDomainStore,
) : TenantDomainService {
  override fun create(model: TenantDomainModel): TenantDomainModel =
      tenantDomainStore.create(model)

  override fun getByOrgGuid(orgGuid: UUID): Set<TenantDomainModel> =
      tenantDomainStore.getByOrgGuid(orgGuid)

  override fun delete(orgGuid: UUID, domain: String): Unit =
      tenantDomainStore.delete(orgGuid, domain)
}

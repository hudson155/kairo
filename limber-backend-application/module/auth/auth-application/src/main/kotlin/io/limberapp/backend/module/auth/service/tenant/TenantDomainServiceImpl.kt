package io.limberapp.backend.module.auth.service.tenant

import com.google.inject.Inject
import com.piperframework.util.ifNull
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.auth.exception.tenant.TenantDomainNotFound
import io.limberapp.backend.module.auth.model.tenant.TenantDomainModel
import io.limberapp.backend.module.auth.store.tenant.TenantDomainStore
import java.util.*

internal class TenantDomainServiceImpl @Inject constructor(
  private val tenantDomainStore: TenantDomainStore
) : TenantDomainService {
  override fun create(model: TenantDomainModel) = tenantDomainStore.create(model)

  override fun getByOrgGuid(orgGuid: UUID) = tenantDomainStore.get(orgGuid = orgGuid).toSet()

  override fun delete(orgGuid: UUID, domain: String) {
    tenantDomainStore.get(orgGuid = orgGuid, domain = domain)
      .singleNullOrThrow()
      .ifNull { throw TenantDomainNotFound() }
    tenantDomainStore.delete(domain)
  }
}

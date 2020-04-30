package io.limberapp.backend.module.auth.service.tenant

import com.google.inject.Inject
import io.limberapp.backend.module.auth.exception.tenant.TenantDomainNotFound
import io.limberapp.backend.module.auth.model.tenant.TenantDomainModel
import io.limberapp.backend.module.auth.store.tenant.TenantDomainStore
import java.util.UUID

internal class TenantDomainServiceImpl @Inject constructor(
    private val tenantDomainStore: TenantDomainStore
) : TenantDomainService {
    override fun create(model: TenantDomainModel) = tenantDomainStore.create(model)

    override fun getByOrgGuid(orgGuid: UUID) = tenantDomainStore.getByOrgGuid(orgGuid)

    override fun delete(orgGuid: UUID, domain: String) {
        if (tenantDomainStore.get(domain)?.orgGuid != orgGuid) throw TenantDomainNotFound()
        tenantDomainStore.delete(domain)
    }
}

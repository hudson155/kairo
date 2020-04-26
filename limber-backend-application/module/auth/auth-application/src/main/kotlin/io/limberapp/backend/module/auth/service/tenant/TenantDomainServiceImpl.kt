package io.limberapp.backend.module.auth.service.tenant

import com.google.inject.Inject
import io.limberapp.backend.module.auth.model.tenant.TenantDomainModel
import io.limberapp.backend.module.auth.store.tenant.TenantDomainStore
import java.util.UUID

internal class TenantDomainServiceImpl @Inject constructor(
    private val tenantDomainStore: TenantDomainStore
) : TenantDomainService {
    override fun create(orgGuid: UUID, model: TenantDomainModel) = tenantDomainStore.create(orgGuid, model)

    override fun delete(orgGuid: UUID, domain: String) = tenantDomainStore.delete(orgGuid, domain)
}

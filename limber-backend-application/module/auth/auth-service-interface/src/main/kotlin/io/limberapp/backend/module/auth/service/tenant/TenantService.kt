package io.limberapp.backend.module.auth.service.tenant

import io.limberapp.backend.module.auth.model.tenant.TenantModel
import java.util.UUID

interface TenantService {
    fun create(model: TenantModel)

    fun get(orgId: UUID): TenantModel?

    fun getByDomain(domain: String): TenantModel?

    fun getByAuth0ClientId(auth0ClientId: String): TenantModel?

    fun update(orgId: UUID, update: TenantModel.Update): TenantModel

    fun delete(orgId: UUID)
}

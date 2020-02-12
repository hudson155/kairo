package io.limberapp.backend.module.auth.service.tenant

import io.limberapp.backend.module.auth.model.tenant.TenantModel

interface TenantService {

    fun create(model: TenantModel)

    fun get(domain: String): TenantModel?

    fun getByAuth0ClientId(auth0ClientId: String): TenantModel?

    fun update(domain: String, update: TenantModel.Update): TenantModel

    fun delete(domain: String)
}

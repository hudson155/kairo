package io.limberapp.backend.module.auth.service.tenant

import io.limberapp.backend.LimberModule
import io.limberapp.backend.module.auth.model.tenant.TenantModel
import java.util.*

@LimberModule.Auth
interface TenantService {
  fun create(model: TenantModel): TenantModel

  fun get(orgGuid: UUID): TenantModel?

  fun getByAuth0ClientId(auth0ClientId: String): TenantModel?

  fun getByDomain(domain: String): TenantModel?

  fun update(orgGuid: UUID, update: TenantModel.Update): TenantModel

  fun delete(orgGuid: UUID)
}

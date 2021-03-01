package io.limberapp.service.tenant

import io.limberapp.model.tenant.TenantModel
import java.util.UUID

interface TenantService {
  fun create(model: TenantModel): TenantModel

  operator fun get(orgGuid: UUID): TenantModel?

  fun getByAuth0ClientId(auth0ClientId: String): TenantModel?

  fun getByDomain(domain: String): TenantModel?

  fun update(orgGuid: UUID, update: TenantModel.Update): TenantModel

  fun delete(orgGuid: UUID)
}

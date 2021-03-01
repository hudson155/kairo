package io.limberapp.service.tenant

import com.google.inject.Inject
import io.limberapp.model.tenant.TenantModel
import io.limberapp.store.tenant.TenantStore
import java.util.UUID

internal class TenantServiceImpl @Inject constructor(
    private val tenantStore: TenantStore,
) : TenantService {
  override fun create(model: TenantModel): TenantModel =
      tenantStore.create(model)

  override fun get(orgGuid: UUID): TenantModel? =
      tenantStore[orgGuid]

  override fun getByAuth0ClientId(auth0ClientId: String): TenantModel? =
      tenantStore.getByAuth0ClientId(auth0ClientId)

  override fun getByDomain(domain: String): TenantModel? =
      tenantStore.getByDomain(domain)

  override fun update(orgGuid: UUID, update: TenantModel.Update): TenantModel =
      tenantStore.update(orgGuid, update)

  override fun delete(orgGuid: UUID): Unit =
      tenantStore.delete(orgGuid)
}

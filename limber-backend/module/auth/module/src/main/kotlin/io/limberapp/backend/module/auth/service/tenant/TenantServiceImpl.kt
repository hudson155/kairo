package io.limberapp.backend.module.auth.service.tenant

import com.google.inject.Inject
import io.limberapp.backend.module.auth.model.tenant.TenantModel
import io.limberapp.backend.module.auth.store.tenant.TenantStore
import java.util.*

internal class TenantServiceImpl @Inject constructor(
  private val tenantStore: TenantStore,
) : TenantService {
  override fun create(model: TenantModel) =
    tenantStore.create(model)

  override fun get(orgGuid: UUID) =
    tenantStore.get(orgGuid)

  override fun getByAuth0ClientId(auth0ClientId: String) =
    tenantStore.getByAuth0ClientId(auth0ClientId)

  override fun getByDomain(domain: String) =
    tenantStore.getByDomain(domain)

  override fun update(orgGuid: UUID, update: TenantModel.Update) =
    tenantStore.update(orgGuid, update)

  override fun delete(orgGuid: UUID) =
    tenantStore.delete(orgGuid)
}

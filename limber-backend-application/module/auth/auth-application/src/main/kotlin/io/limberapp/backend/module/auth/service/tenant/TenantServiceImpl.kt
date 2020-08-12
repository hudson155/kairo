package io.limberapp.backend.module.auth.service.tenant

import com.google.inject.Inject
import com.piperframework.finder.Finder
import io.limberapp.backend.module.auth.model.tenant.TenantFinder
import io.limberapp.backend.module.auth.model.tenant.TenantModel
import io.limberapp.backend.module.auth.store.tenant.TenantStore
import java.util.*

internal class TenantServiceImpl @Inject constructor(
  private val tenantStore: TenantStore
) : TenantService, Finder<TenantModel, TenantFinder> by tenantStore {
  override fun create(model: TenantModel) =
    tenantStore.create(model)

  override fun update(orgGuid: UUID, update: TenantModel.Update) =
    tenantStore.update(orgGuid, update)

  override fun delete(orgGuid: UUID) =
    tenantStore.delete(orgGuid)
}

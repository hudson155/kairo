package io.limberapp.backend.module.auth.service.tenant

import com.piperframework.finder.Finder
import io.limberapp.backend.LimberModule
import io.limberapp.backend.module.auth.model.tenant.TenantFinder
import io.limberapp.backend.module.auth.model.tenant.TenantModel
import java.util.*

@LimberModule.Auth
interface TenantService : Finder<TenantModel, TenantFinder> {
  fun create(model: TenantModel): TenantModel

  fun update(orgGuid: UUID, update: TenantModel.Update): TenantModel

  fun delete(orgGuid: UUID)
}

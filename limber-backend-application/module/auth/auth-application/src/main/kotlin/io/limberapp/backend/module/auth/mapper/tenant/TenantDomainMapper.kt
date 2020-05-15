package io.limberapp.backend.module.auth.mapper.tenant

import com.google.inject.Inject
import io.limberapp.backend.module.auth.model.tenant.TenantDomainModel
import io.limberapp.backend.module.auth.rep.tenant.TenantDomainRep
import java.time.Clock
import java.time.LocalDateTime
import java.util.*

internal class TenantDomainMapper @Inject constructor(
  private val clock: Clock
) {
  fun model(orgGuid: UUID, rep: TenantDomainRep.Creation) = TenantDomainModel(
    createdDate = LocalDateTime.now(clock),
    orgGuid = orgGuid,
    domain = rep.domain
  )

  fun completeRep(model: TenantDomainModel) = TenantDomainRep.Complete(
    createdDate = model.createdDate,
    domain = model.domain
  )
}

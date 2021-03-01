package io.limberapp.mapper.tenant

import com.google.inject.Inject
import io.limberapp.model.tenant.TenantDomainModel
import io.limberapp.rep.tenant.TenantDomainRep
import java.time.Clock
import java.time.ZonedDateTime
import java.util.UUID

internal class TenantDomainMapper @Inject constructor(
    private val clock: Clock,
) {
  fun model(orgGuid: UUID, rep: TenantDomainRep.Creation): TenantDomainModel =
      TenantDomainModel(
          createdDate = ZonedDateTime.now(clock),
          orgGuid = orgGuid,
          domain = rep.domain.toLowerCase(),
      )

  fun completeRep(model: TenantDomainModel): TenantDomainRep.Complete =
      TenantDomainRep.Complete(domain = model.domain)
}

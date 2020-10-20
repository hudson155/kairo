package io.limberapp.backend.module.auth.mapper.tenant

import com.google.inject.Inject
import io.limberapp.backend.module.auth.model.tenant.TenantDomainModel
import io.limberapp.backend.module.auth.model.tenant.TenantModel
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import java.time.Clock
import java.time.ZonedDateTime

internal class TenantMapper @Inject constructor(
    private val clock: Clock,
    private val tenantDomainMapper: TenantDomainMapper,
) {
  fun model(rep: TenantRep.Creation) = TenantModel(
      createdDate = ZonedDateTime.now(clock),
      orgGuid = rep.orgGuid,
      name = rep.name,
      auth0ClientId = rep.auth0ClientId
  )

  fun completeRep(model: TenantModel, domains: Set<TenantDomainModel>) = TenantRep.Complete(
      createdDate = model.createdDate,
      orgGuid = model.orgGuid,
      name = model.name,
      auth0ClientId = model.auth0ClientId,
      domains = domains.map { tenantDomainMapper.completeRep(it) }.toSet()
  )

  fun update(rep: TenantRep.Update) = TenantModel.Update(
      name = rep.name,
      auth0ClientId = rep.auth0ClientId
  )
}

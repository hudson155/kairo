package io.limberapp.backend.module.auth.mapper.tenant

import com.google.inject.Inject
import io.limberapp.backend.module.auth.model.tenant.TenantModel
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import java.time.Clock
import java.time.LocalDateTime

internal class TenantMapper @Inject constructor(
    private val clock: Clock,
    private val tenantDomainMapper: TenantDomainMapper
) {
    fun model(rep: TenantRep.Creation) = TenantModel(
        created = LocalDateTime.now(clock),
        orgId = rep.orgId,
        auth0ClientId = rep.auth0ClientId,
        domains = setOf(tenantDomainMapper.model(rep.domain))
    )

    fun completeRep(model: TenantModel) = TenantRep.Complete(
        created = model.created,
        orgId = model.orgId,
        auth0ClientId = model.auth0ClientId,
        domains = model.domains.map { tenantDomainMapper.completeRep(it) }
    )

    fun update(rep: TenantRep.Update) = TenantModel.Update(
        auth0ClientId = rep.auth0ClientId
    )
}

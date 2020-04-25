package io.limberapp.backend.module.auth.mapper.tenant

import com.google.inject.Inject
import io.limberapp.backend.module.auth.model.tenant.TenantDomainModel
import io.limberapp.backend.module.auth.rep.tenant.TenantDomainRep
import java.time.Clock
import java.time.LocalDateTime

internal class TenantDomainMapper @Inject constructor(
    private val clock: Clock
) {
    fun model(rep: TenantDomainRep.Creation) = TenantDomainModel(
        created = LocalDateTime.now(clock),
        domain = rep.domain
    )

    fun completeRep(model: TenantDomainModel) = TenantDomainRep.Complete(
        created = model.created,
        domain = model.domain
    )
}

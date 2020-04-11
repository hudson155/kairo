package io.limberapp.backend.module.auth.mapper.tenant

import com.google.inject.Inject
import io.limberapp.backend.module.auth.model.tenant.TenantModel
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import java.time.Clock
import java.time.LocalDateTime

internal class TenantMapper @Inject constructor(
    private val clock: Clock
) {

    fun model(rep: TenantRep.Creation) = TenantModel(
        domain = rep.domain,
        created = LocalDateTime.now(clock),
        orgId = rep.orgId,
        auth0ClientId = rep.auth0ClientId
    )

    fun completeRep(model: TenantModel) = TenantRep.Complete(
        domain = model.domain,
        created = model.created,
        orgId = model.orgId,
        auth0ClientId = model.auth0ClientId
    )

    fun update(rep: TenantRep.Update) = TenantModel.Update(
        domain = rep.domain,
        orgId = rep.orgId,
        auth0ClientId = rep.auth0ClientId
    )
}

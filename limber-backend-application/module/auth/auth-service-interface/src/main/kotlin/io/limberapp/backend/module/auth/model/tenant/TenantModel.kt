package io.limberapp.backend.module.auth.model.tenant

import java.time.LocalDateTime
import java.util.UUID

data class TenantModel(
    val created: LocalDateTime,
    val orgId: UUID,
    val auth0ClientId: String,
    val domains: Set<TenantDomainModel>
) {

    data class Update(
        val auth0ClientId: String?
    )
}

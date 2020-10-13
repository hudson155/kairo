package io.limberapp.backend.module.auth.model.tenant

import java.time.LocalDateTime
import java.util.*

data class TenantDomainModel(
    val createdDate: LocalDateTime,
    val orgGuid: UUID,
    val domain: String,
)

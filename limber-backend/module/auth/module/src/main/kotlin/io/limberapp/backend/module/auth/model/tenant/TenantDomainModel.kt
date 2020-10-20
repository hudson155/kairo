package io.limberapp.backend.module.auth.model.tenant

import java.time.ZonedDateTime
import java.util.*

data class TenantDomainModel(
    val createdDate: ZonedDateTime,
    val orgGuid: UUID,
    val domain: String,
)

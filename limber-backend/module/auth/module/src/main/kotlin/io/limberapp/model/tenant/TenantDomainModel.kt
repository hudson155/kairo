package io.limberapp.model.tenant

import java.time.ZonedDateTime
import java.util.UUID

data class TenantDomainModel(
    val createdDate: ZonedDateTime,
    val orgGuid: UUID,
    val domain: String,
)

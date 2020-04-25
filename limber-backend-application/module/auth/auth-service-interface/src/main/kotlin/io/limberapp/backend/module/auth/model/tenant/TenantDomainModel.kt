package io.limberapp.backend.module.auth.model.tenant

import java.time.LocalDateTime

data class TenantDomainModel(
    val createdDate: LocalDateTime,
    val domain: String
)

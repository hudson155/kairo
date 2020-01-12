package io.limberapp.backend.module.orgs.entity.org

import java.time.LocalDateTime
import java.util.UUID

data class MembershipEntity(
    val created: LocalDateTime,
    val userId: UUID
)

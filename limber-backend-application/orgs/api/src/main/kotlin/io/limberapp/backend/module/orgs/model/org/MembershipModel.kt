package io.limberapp.backend.module.orgs.model.org

import java.time.LocalDateTime
import java.util.UUID

data class MembershipModel(
    val created: LocalDateTime,
    val userId: UUID
)

package io.limberapp.backend.module.orgs.entity.org

import io.limberapp.framework.entity.CompleteSubentity
import io.limberapp.framework.entity.CreationSubentity
import java.time.LocalDateTime
import java.util.UUID

object MembershipEntity {

    data class Creation(
        override val created: LocalDateTime,
        val userId: UUID
    ) : CreationSubentity()

    data class Complete(
        override val created: LocalDateTime,
        val userId: UUID
    ) : CompleteSubentity()
}

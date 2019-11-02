package io.limberapp.backend.module.orgs.model.org

import io.limberapp.framework.model.CompleteModelWithoutId
import io.limberapp.framework.model.CreationModelWithoutId
import java.time.LocalDateTime
import java.util.UUID

internal object MembershipModel {

    data class Creation(
        override val created: LocalDateTime,
        override val version: Int,
        val userId: UUID
    ) : CreationModelWithoutId()

    data class Complete(
        override val created: LocalDateTime,
        override val version: Int,
        val userId: UUID
    ) : CompleteModelWithoutId()
}

package io.limberapp.backend.module.orgs.rep.membership

import io.limberapp.framework.rep.CompleteRepWithoutId
import io.limberapp.framework.rep.CreationRep
import java.time.LocalDateTime
import java.util.UUID

object MembershipRep {

    data class Creation(
        val userId: UUID
    ) : CreationRep() {
        override fun validate() = Unit
    }

    data class Complete(
        override val created: LocalDateTime,
        val userId: UUID
    ) : CompleteRepWithoutId()
}

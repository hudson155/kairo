package io.limberapp.backend.module.orgs.rep.membership

import io.limberapp.framework.rep.CompleteSubrep
import io.limberapp.framework.rep.CreationSubrep
import java.time.LocalDateTime
import java.util.UUID

object MembershipRep {

    data class Creation(
        val userId: UUID
    ) : CreationSubrep() {
        override fun validate() = Unit
    }

    data class Complete(
        override val created: LocalDateTime,
        val userId: UUID
    ) : CompleteSubrep()
}

package io.limberapp.backend.module.orgs.rep.membership

import com.piperframework.rep.CompleteSubrep
import com.piperframework.rep.CreationSubrep
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

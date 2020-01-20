package io.limberapp.backend.module.orgs.rep.org

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import java.time.LocalDateTime
import java.util.UUID

object MembershipRep {

    data class Creation(
        val userId: UUID
    ) : CreationRep {
        override fun validate() = Unit
    }

    data class Complete(
        override val created: LocalDateTime,
        val userId: UUID
    ) : CompleteRep
}

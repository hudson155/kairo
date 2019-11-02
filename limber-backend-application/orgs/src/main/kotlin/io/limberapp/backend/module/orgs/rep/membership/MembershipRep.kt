package io.limberapp.backend.module.orgs.rep.membership

import io.limberapp.framework.rep.CompleteRepWithoutId
import java.time.LocalDateTime
import java.util.UUID

object MembershipRep {

    data class Complete(
        override val created: LocalDateTime,
        val userId: UUID,
        val userName: String
    ) : CompleteRepWithoutId()
}

package io.limberapp.backend.module.orgs.model.org

import io.limberapp.framework.model.CompleteSubmodel
import io.limberapp.framework.model.CreationSubmodel
import java.time.LocalDateTime
import java.util.UUID

object MembershipModel {

    data class Creation(
        override val created: LocalDateTime,
        val userId: UUID
    ) : CreationSubmodel()

    data class Complete(
        override val created: LocalDateTime,
        val userId: UUID
    ) : CompleteSubmodel()
}

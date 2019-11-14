package io.limberapp.backend.module.orgs.entity.org

import io.limberapp.framework.entity.CompleteEntity
import io.limberapp.framework.entity.CreationEntity
import io.limberapp.framework.entity.UpdateEntity
import java.time.LocalDateTime
import java.util.UUID

object OrgEntity {

    data class Creation(
        override val id: UUID,
        override val created: LocalDateTime,
        override val version: Int,
        val name: String,
        val members: List<MembershipEntity.Complete>
    ) : CreationEntity()

    data class Complete(
        override val id: UUID,
        override val created: LocalDateTime,
        override val version: Int,
        val name: String,
        val members: List<MembershipEntity.Complete>
    ) : CompleteEntity()

    data class Update(
        val name: String?
    ) : UpdateEntity()
}

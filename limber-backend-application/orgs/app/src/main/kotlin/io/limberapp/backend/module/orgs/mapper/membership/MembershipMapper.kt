package io.limberapp.backend.module.orgs.mapper.membership

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.entity.org.MembershipEntity
import io.limberapp.backend.module.orgs.rep.membership.MembershipRep
import java.time.Clock
import java.time.LocalDateTime

internal class MembershipMapper @Inject constructor(
    private val clock: Clock
) {

    fun creationEntity(rep: MembershipRep.Creation) = MembershipEntity.Creation(
        created = LocalDateTime.now(clock),
        userId = rep.userId
    )

    fun completeRep(entity: MembershipEntity.Complete) = MembershipRep.Complete(
        created = entity.created,
        userId = entity.userId
    )
}

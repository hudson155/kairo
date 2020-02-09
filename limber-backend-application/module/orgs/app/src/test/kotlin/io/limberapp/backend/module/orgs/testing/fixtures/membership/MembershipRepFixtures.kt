package io.limberapp.backend.module.orgs.testing.fixtures.membership

import io.limberapp.backend.module.orgs.rep.org.MembershipRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import java.time.LocalDateTime
import java.util.UUID

internal object MembershipRepFixtures {

    data class Fixture(
        val creation: (userId: UUID) -> MembershipRep.Creation,
        val complete: ResourceTest.(userId: UUID) -> MembershipRep.Complete
    )

    val fixture = Fixture({ userId ->
        MembershipRep.Creation(userId)
    }, { userId ->
        MembershipRep.Complete(
            created = LocalDateTime.now(fixedClock),
            userId = userId
        )
    })
}

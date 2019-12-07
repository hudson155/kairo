package io.limberapp.backend.module.orgs.testing.fixtures.membership

import io.limberapp.backend.module.orgs.rep.membership.MembershipRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import java.time.LocalDateTime
import java.util.UUID

internal object MembershipRepFixtures {

    val Creation = listOf(
        MembershipRep.Creation(UUID.randomUUID()),
        MembershipRep.Creation(UUID.randomUUID())
    )

    val Complete = Creation.map<MembershipRep.Creation, ResourceTest.() -> MembershipRep.Complete> {
        {
            MembershipRep.Complete(
                created = LocalDateTime.now(fixedClock),
                userId = it.userId
            )
        }
    }
}

package io.limberapp.backend.module.users.testing.fixtures.account

import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import java.time.LocalDateTime
import java.util.UUID

internal object UserRepFixtures {
    data class Fixture(
        val creation: (orgId: UUID) -> UserRep.Creation,
        val complete: ResourceTest.(orgId: UUID, idSeed: Int) -> UserRep.Complete
    )

    val jeffHudsonFixture = Fixture({ orgId ->
        UserRep.Creation(
            orgId = orgId,
            firstName = "Jeff",
            lastName = "Hudson",
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null
        )
    }, { orgId, idSeed ->
        UserRep.Complete(
            id = deterministicUuidGenerator[idSeed],
            created = LocalDateTime.now(fixedClock),
            roles = emptyList(),
            orgId = orgId,
            firstName = "Jeff",
            lastName = "Hudson",
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null
        )
    })

    val billGatesFixture = Fixture({ orgId ->
        UserRep.Creation(
            orgId = orgId,
            firstName = "Bill",
            lastName = "Gates",
            emailAddress = "bill.gates@microsoft.com",
            profilePhotoUrl = "https://pbs.twimg.com/profile_images/988775660163252226/XpgonN0X_400x400.jpg"
        )
    }, { orgId, idSeed ->
        UserRep.Complete(
            id = deterministicUuidGenerator[idSeed],
            created = LocalDateTime.now(fixedClock),
            roles = emptyList(),
            orgId = orgId,
            firstName = "Bill",
            lastName = "Gates",
            emailAddress = "bill.gates@microsoft.com",
            profilePhotoUrl = "https://pbs.twimg.com/profile_images/988775660163252226/XpgonN0X_400x400.jpg"
        )
    })
}

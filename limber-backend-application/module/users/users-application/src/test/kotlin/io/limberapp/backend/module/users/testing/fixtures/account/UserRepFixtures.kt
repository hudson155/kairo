package io.limberapp.backend.module.users.testing.fixtures.account

import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import java.time.LocalDateTime
import java.util.UUID

internal object UserRepFixtures {
    data class Fixture(
        val creation: (orgGuid: UUID) -> UserRep.Creation,
        val complete: ResourceTest.(orgGuid: UUID, idSeed: Int) -> UserRep.Complete
    )

    val jeffHudsonFixture = Fixture({ orgGuid ->
        UserRep.Creation(
            orgGuid = orgGuid,
            firstName = "Jeff",
            lastName = "Hudson",
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null
        )
    }, { orgGuid, idSeed ->
        UserRep.Complete(
            guid = deterministicUuidGenerator[idSeed],
            createdDate = LocalDateTime.now(fixedClock),
            roles = emptySet(),
            orgGuid = orgGuid,
            firstName = "Jeff",
            lastName = "Hudson",
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null
        )
    })

    val billGatesFixture = Fixture({ orgGuid ->
        UserRep.Creation(
            orgGuid = orgGuid,
            firstName = "Bill",
            lastName = "Gates",
            emailAddress = "bill.gates@microsoft.com",
            profilePhotoUrl = "https://pbs.twimg.com/profile_images/988775660163252226/XpgonN0X_400x400.jpg"
        )
    }, { orgGuid, idSeed ->
        UserRep.Complete(
            guid = deterministicUuidGenerator[idSeed],
            createdDate = LocalDateTime.now(fixedClock),
            roles = emptySet(),
            orgGuid = orgGuid,
            firstName = "Bill",
            lastName = "Gates",
            emailAddress = "bill.gates@microsoft.com",
            profilePhotoUrl = "https://pbs.twimg.com/profile_images/988775660163252226/XpgonN0X_400x400.jpg"
        )
    })
}

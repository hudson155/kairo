package io.limberapp.backend.module.users.testing.fixtures.user

import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import java.time.LocalDateTime

internal object UserRepFixtures {

    data class Fixture(
        val creation: () -> UserRep.Creation,
        val complete: ResourceTest.(idSeed: Int) -> UserRep.Complete
    )

    operator fun get(i: Int) = fixtures[i]

    private val fixtures = listOf(
        Fixture({
            UserRep.Creation(
                firstName = "Jeff",
                lastName = "Hudson",
                emailAddress = "jhudson@jhudson.ca",
                profilePhotoUrl = null
            )
        }, { idSeed ->
            UserRep.Complete(
                id = deterministicUuidGenerator[idSeed],
                created = LocalDateTime.now(fixedClock),
                firstName = "Jeff",
                lastName = "Hudson",
                emailAddress = "jhudson@jhudson.ca",
                profilePhotoUrl = null,
                roles = emptySet()
            )
        }),
        Fixture({
            UserRep.Creation(
                firstName = "Bill",
                lastName = "Gates",
                emailAddress = "bill.gates@microsoft.com",
                profilePhotoUrl = "https://pbs.twimg.com/profile_images/988775660163252226/XpgonN0X_400x400.jpg"
            )
        }, { idSeed ->
            UserRep.Complete(
                id = deterministicUuidGenerator[idSeed],
                created = LocalDateTime.now(fixedClock),
                firstName = "Bill",
                lastName = "Gates",
                emailAddress = "bill.gates@microsoft.com",
                profilePhotoUrl = "https://pbs.twimg.com/profile_images/988775660163252226/XpgonN0X_400x400.jpg",
                roles = emptySet()
            )
        })
    )
}

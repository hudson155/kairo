package io.limberapp.backend.module.users.testing.fixtures.user

import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import java.time.LocalDateTime

internal object UserRepFixtures {

    val Creation = listOf(
        UserRep.Creation(
            firstName = "Jeff",
            lastName = "Hudson",
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null
        ),
        UserRep.Creation(
            firstName = "Bill",
            lastName = "Gates",
            emailAddress = "bill.gates@microsoft.com",
            profilePhotoUrl = "https://pbs.twimg.com/profile_images/988775660163252226/XpgonN0X_400x400.jpg"
        )
    )

    val Complete = Creation.map { rep ->
        fun ResourceTest.(idSeed: Int): UserRep.Complete {
            return UserRep.Complete(
                id = deterministicUuidGenerator[idSeed],
                created = LocalDateTime.now(fixedClock),
                firstName = rep.firstName,
                lastName = rep.lastName,
                emailAddress = rep.emailAddress,
                profilePhotoUrl = rep.profilePhotoUrl,
                roles = emptySet()
            )
        }
    }
}

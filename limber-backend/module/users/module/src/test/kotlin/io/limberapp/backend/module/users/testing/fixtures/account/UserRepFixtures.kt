package io.limberapp.backend.module.users.testing.fixtures.account

import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.common.util.time.inUTC
import io.limberapp.testing.integration.LimberIntegrationTest
import java.time.ZonedDateTime
import java.util.*

internal object UserRepFixtures {
  data class Fixture(
      val creation: (orgGuid: UUID) -> UserRep.Creation,
      val complete: LimberIntegrationTest.(orgGuid: UUID, idSeed: Int) -> UserRep.Complete,
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
        guid = uuidGenerator[idSeed],
        createdDate = ZonedDateTime.now(clock).inUTC(),
        roles = emptySet(),
        orgGuid = orgGuid,
        firstName = "Jeff",
        lastName = "Hudson",
        fullName = "Jeff Hudson",
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
        guid = uuidGenerator[idSeed],
        createdDate = ZonedDateTime.now(clock).inUTC(),
        roles = emptySet(),
        orgGuid = orgGuid,
        firstName = "Bill",
        lastName = "Gates",
        fullName = "Bill Gates",
        emailAddress = "bill.gates@microsoft.com",
        profilePhotoUrl = "https://pbs.twimg.com/profile_images/988775660163252226/XpgonN0X_400x400.jpg"
    )
  })
}

internal fun UserRep.Complete.summary() = UserRep.Summary(
    guid = guid,
    createdDate = createdDate,
    orgGuid = orgGuid,
    firstName = firstName,
    lastName = lastName,
    fullName = fullName,
    profilePhotoUrl = profilePhotoUrl
)

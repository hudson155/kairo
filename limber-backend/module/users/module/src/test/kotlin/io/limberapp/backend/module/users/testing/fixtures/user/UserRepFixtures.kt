package io.limberapp.backend.module.users.testing.fixtures.user

import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.common.permissions.limberPermissions.LimberPermissions
import io.limberapp.testing.integration.AbstractIntegrationTest
import java.util.UUID

internal object UserRepFixtures {
  data class Fixture(
      val creation: (orgGuid: UUID) -> UserRep.Creation,
      val complete: AbstractIntegrationTest.(orgGuid: UUID, idSeed: Int) -> UserRep.Complete,
  )

  val jeffHudsonFixture: Fixture = Fixture({ orgGuid ->
    UserRep.Creation(
        orgGuid = orgGuid,
        emailAddress = "jeff.hudson@limberapp.io",
        firstName = "Jeff",
        lastName = "Hudson",
        profilePhotoUrl = null,
    )
  }, { orgGuid, idSeed ->
    UserRep.Complete(
        guid = guids[idSeed],
        permissions = LimberPermissions.none(),
        orgGuid = orgGuid,
        emailAddress = "jeff.hudson@limberapp.io",
        firstName = "Jeff",
        lastName = "Hudson",
        fullName = "Jeff Hudson",
        profilePhotoUrl = null,
    )
  })

  val billGatesFixture: Fixture = Fixture({ orgGuid ->
    UserRep.Creation(
        orgGuid = orgGuid,
        emailAddress = "bill.gates@microsoft.com",
        firstName = "Bill",
        lastName = "Gates",
        profilePhotoUrl = "https://pbs.twimg.com/profile_images/988775660163252226/XpgonN0X_400x400.jpg",
    )
  }, { orgGuid, idSeed ->
    UserRep.Complete(
        guid = guids[idSeed],
        permissions = LimberPermissions.none(),
        orgGuid = orgGuid,
        emailAddress = "bill.gates@microsoft.com",
        firstName = "Bill",
        lastName = "Gates",
        fullName = "Bill Gates",
        profilePhotoUrl = "https://pbs.twimg.com/profile_images/988775660163252226/XpgonN0X_400x400.jpg",
    )
  })
}

internal fun UserRep.Complete.summary(): UserRep.Summary = UserRep.Summary(
    guid = guid,
    orgGuid = orgGuid,
    firstName = firstName,
    lastName = lastName,
    fullName = fullName,
    profilePhotoUrl = profilePhotoUrl,
)

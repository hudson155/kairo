package io.limberapp.rep.user

import io.limberapp.permissions.limber.LimberPermissions
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
        fullName = "Jeff Hudson",
        profilePhotoUrl = null,
    )
  }, { orgGuid, idSeed ->
    UserRep.Complete(
        guid = guids[idSeed],
        permissions = LimberPermissions.none(),
        orgGuid = orgGuid,
        emailAddress = "jeff.hudson@limberapp.io",
        fullName = "Jeff Hudson",
        initials = "JH",
        profilePhotoUrl = null,
    )
  })

  val billGatesFixture: Fixture = Fixture({ orgGuid ->
    UserRep.Creation(
        orgGuid = orgGuid,
        emailAddress = "bill.gates@microsoft.com",
        fullName = "Bill Gates",
        profilePhotoUrl = "https://pbs.twimg.com/profile_images/988775660163252226/XpgonN0X_400x400.jpg",
    )
  }, { orgGuid, idSeed ->
    UserRep.Complete(
        guid = guids[idSeed],
        permissions = LimberPermissions.none(),
        orgGuid = orgGuid,
        emailAddress = "bill.gates@microsoft.com",
        fullName = "Bill Gates",
        initials = "BG",
        profilePhotoUrl = "https://pbs.twimg.com/profile_images/988775660163252226/XpgonN0X_400x400.jpg",
    )
  })
}

internal fun UserRep.Complete.summary(): UserRep.Summary = UserRep.Summary(
    guid = guid,
    orgGuid = orgGuid,
    fullName = fullName,
    initials = initials,
    profilePhotoUrl = profilePhotoUrl,
)

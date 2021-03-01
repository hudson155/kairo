package io.limberapp.rep.tenant

import io.limberapp.testing.integration.IntegrationTest
import java.util.UUID

internal object TenantRepFixtures {
  data class Fixture(
      val creation: (orgGuid: UUID) -> TenantRep.Creation,
      val complete: IntegrationTest.(orgGuid: UUID) -> TenantRep.Complete,
  )

  val limberappFixture: Fixture = Fixture({ orgGuid ->
    TenantRep.Creation(
        orgGuid = orgGuid,
        auth0ClientId = "abcdefghijklmnopqrstuvwxyzabcdef",
    )
  }, { orgGuid ->
    TenantRep.Complete(
        orgGuid = orgGuid,
        auth0ClientId = "abcdefghijklmnopqrstuvwxyzabcdef",
        domains = emptySet(),
    )
  })

  val someclientFixture: Fixture = Fixture({ orgGuid ->
    TenantRep.Creation(
        orgGuid = orgGuid,
        auth0ClientId = "01234567890123456789012345678901",
    )
  }, { orgGuid ->
    TenantRep.Complete(
        orgGuid = orgGuid,
        auth0ClientId = "01234567890123456789012345678901",
        domains = emptySet(),
    )
  })
}

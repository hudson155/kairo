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
        auth0ClientId = "org_abcdefghijklmnop",
    )
  }, { orgGuid ->
    TenantRep.Complete(
        orgGuid = orgGuid,
        auth0ClientId = "org_abcdefghijklmnop",
        domains = emptySet(),
    )
  })

  val someclientFixture: Fixture = Fixture({ orgGuid ->
    TenantRep.Creation(
        orgGuid = orgGuid,
        auth0ClientId = "org_0123456789012345",
    )
  }, { orgGuid ->
    TenantRep.Complete(
        orgGuid = orgGuid,
        auth0ClientId = "org_0123456789012345",
        domains = emptySet(),
    )
  })
}

package io.limberapp.backend.module.auth.testing.fixtures.tenant

import io.limberapp.backend.module.auth.rep.tenant.TenantDomainRep
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.testing.IntegrationTest
import io.limberapp.common.util.time.inUTC
import java.time.ZonedDateTime
import java.util.*

internal object TenantRepFixtures {
  data class Fixture(
      val creation: (orgGuid: UUID) -> TenantRep.Creation,
      val complete: IntegrationTest.(orgGuid: UUID) -> TenantRep.Complete,
  )

  val limberappFixture = Fixture({ orgGuid ->
    TenantRep.Creation(
        orgGuid = orgGuid,
        name = "Limber",
        auth0ClientId = "abcdefghijklmnopqrstuvwxyzabcdef",
        domain = TenantDomainRep.Creation("limberapp.io")
    )
  }, { orgGuid ->
    TenantRep.Complete(
        createdDate = ZonedDateTime.now(clock).inUTC(),
        orgGuid = orgGuid,
        name = "Limber",
        auth0ClientId = "abcdefghijklmnopqrstuvwxyzabcdef",
        domains = setOf(
            TenantDomainRep.Complete(
                createdDate = ZonedDateTime.now(clock).inUTC(),
                domain = "limberapp.io"
            )
        )
    )
  })

  val someclientFixture = Fixture({ orgGuid ->
    TenantRep.Creation(
        orgGuid = orgGuid,
        name = "Some Client",
        auth0ClientId = "01234567890123456789012345678901",
        domain = TenantDomainRep.Creation("limber.someclient.com")
    )
  }, { orgGuid ->
    TenantRep.Complete(
        createdDate = ZonedDateTime.now(clock).inUTC(),
        orgGuid = orgGuid,
        name = "Some Client",
        auth0ClientId = "01234567890123456789012345678901",
        domains = setOf(
            TenantDomainRep.Complete(
                createdDate = ZonedDateTime.now(clock).inUTC(),
                domain = "limber.someclient.com"
            )
        )
    )
  })
}

package io.limberapp.backend.module.auth.testing.fixtures.tenant

import io.limberapp.backend.module.auth.rep.tenant.TenantDomainRep
import io.limberapp.backend.module.auth.testing.IntegrationTest
import io.limberapp.common.util.time.inUTC
import java.time.ZonedDateTime

internal object TenantDomainRepFixtures {
  data class Fixture(
      val creation: () -> TenantDomainRep.Creation,
      val complete: IntegrationTest.() -> TenantDomainRep.Complete,
  )

  val limberappFixture = Fixture({
    TenantDomainRep.Creation("limberapp.io")
  }, {
    TenantDomainRep.Complete(createdDate = ZonedDateTime.now(clock).inUTC(), domain = "limberapp.io")
  })

  val someclientFixture = Fixture({
    TenantDomainRep.Creation("limber.someclient.com")
  }, {
    TenantDomainRep.Complete(createdDate = ZonedDateTime.now(clock).inUTC(), domain = "limber.someclient.com")
  })
}

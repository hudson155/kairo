package io.limberapp.rep.tenant

import io.limberapp.testing.integration.IntegrationTest

internal object TenantDomainRepFixtures {
  data class Fixture(
      val creation: () -> TenantDomainRep.Creation,
      val complete: IntegrationTest.() -> TenantDomainRep.Complete,
  )

  val limberappFixture: Fixture = Fixture({
    TenantDomainRep.Creation("limberapp.io")
  }, {
    TenantDomainRep.Complete(domain = "limberapp.io")
  })

  val someclientFixture: Fixture = Fixture({
    TenantDomainRep.Creation("limber.someclient.com")
  }, {
    TenantDomainRep.Complete(domain = "limber.someclient.com")
  })
}

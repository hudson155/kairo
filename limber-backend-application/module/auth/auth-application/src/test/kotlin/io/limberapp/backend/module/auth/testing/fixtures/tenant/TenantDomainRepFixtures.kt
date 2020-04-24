package io.limberapp.backend.module.auth.testing.fixtures.tenant

import io.limberapp.backend.module.auth.rep.tenant.TenantDomainRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import java.time.LocalDateTime

internal object TenantDomainRepFixtures {
    data class Fixture(
        val creation: () -> TenantDomainRep.Creation,
        val complete: ResourceTest.() -> TenantDomainRep.Complete
    )

    val limberappFixture = Fixture({
        TenantDomainRep.Creation("limberapp.io")
    }, {
        TenantDomainRep.Complete(created = LocalDateTime.now(fixedClock), domain = "limberapp.io")
    })

    val someclientFixture = Fixture({
        TenantDomainRep.Creation("limber.someclient.com")
    }, {
        TenantDomainRep.Complete(created = LocalDateTime.now(fixedClock), domain = "limber.someclient.com")
    })
}

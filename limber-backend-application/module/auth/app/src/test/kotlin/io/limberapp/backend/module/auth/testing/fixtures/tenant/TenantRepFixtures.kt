package io.limberapp.backend.module.auth.testing.fixtures.tenant

import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import java.time.LocalDateTime
import java.util.UUID

internal object TenantRepFixtures {

    data class Fixture(
        val creation: (orgId: UUID) -> TenantRep.Creation,
        val complete: ResourceTest.(orgId: UUID) -> TenantRep.Complete
    )

    val limberappFixture = Fixture({ orgId ->
        TenantRep.Creation("limberapp.io", orgId, "abcdefghijklmnopqrstuvwxyz")
    }, { orgId ->
        TenantRep.Complete(
            domain = "limberapp.io",
            orgId = orgId,
            created = LocalDateTime.now(fixedClock),
            auth0ClientId = "abcdefghijklmnopqrstuvwxyz"
        )
    })

    val someclientFixture = Fixture({ orgId ->
        TenantRep.Creation("limber.someclient.com", orgId, "0123456789")
    }, { orgId ->
        TenantRep.Complete(
            domain = "limber.someclient.com",
            orgId = orgId,
            created = LocalDateTime.now(fixedClock),
            auth0ClientId = "0123456789"
        )
    })
}

package io.limberapp.backend.module.auth.testing.fixtures.tenant

import io.limberapp.backend.module.auth.rep.tenant.TenantDomainRep
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
        TenantRep.Creation(orgId, "abcdefghijklmnopqrstuvwxyzabcdef", TenantDomainRep.Creation("limberapp.io"))
    }, { orgId ->
        TenantRep.Complete(
            orgId = orgId,
            created = LocalDateTime.now(fixedClock),
            auth0ClientId = "abcdefghijklmnopqrstuvwxyzabcdef",
            domains = listOf(TenantDomainRep.Complete(created = LocalDateTime.now(fixedClock), domain = "limberapp.io"))
        )
    })

    val someclientFixture = Fixture({ orgId ->
        TenantRep.Creation(orgId, "01234567890123456789012345678901", TenantDomainRep.Creation("limber.someclient.com"))
    }, { orgId ->
        TenantRep.Complete(
            orgId = orgId,
            created = LocalDateTime.now(fixedClock),
            auth0ClientId = "01234567890123456789012345678901",
            domains = listOf(
                TenantDomainRep.Complete(
                    created = LocalDateTime.now(fixedClock),
                    domain = "limber.someclient.com"
                )
            )
        )
    })
}

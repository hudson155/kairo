package io.limberapp.backend.module.auth.testing.fixtures.tenant

import io.limberapp.backend.module.auth.rep.tenant.TenantDomainRep
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import java.time.LocalDateTime
import java.util.UUID

internal object TenantRepFixtures {
    data class Fixture(
        val creation: (orgGuid: UUID) -> TenantRep.Creation,
        val complete: ResourceTest.(orgGuid: UUID) -> TenantRep.Complete
    )

    val limberappFixture = Fixture({ orgGuid ->
        TenantRep.Creation(
            orgGuid = orgGuid,
            auth0ClientId = "abcdefghijklmnopqrstuvwxyzabcdef",
            domain = TenantDomainRep.Creation("limberapp.io")
        )
    }, { orgGuid ->
        TenantRep.Complete(
            orgGuid = orgGuid,
            createdDate = LocalDateTime.now(fixedClock),
            auth0ClientId = "abcdefghijklmnopqrstuvwxyzabcdef",
            domains = setOf(
                TenantDomainRep.Complete(
                    createdDate = LocalDateTime.now(fixedClock),
                    domain = "limberapp.io"
                )
            )
        )
    })

    val someclientFixture = Fixture({ orgGuid ->
        TenantRep.Creation(
            orgGuid = orgGuid,
            auth0ClientId = "01234567890123456789012345678901",
            domain = TenantDomainRep.Creation("limber.someclient.com")
        )
    }, { orgGuid ->
        TenantRep.Complete(
            orgGuid = orgGuid,
            createdDate = LocalDateTime.now(fixedClock),
            auth0ClientId = "01234567890123456789012345678901",
            domains = setOf(
                TenantDomainRep.Complete(
                    createdDate = LocalDateTime.now(fixedClock),
                    domain = "limber.someclient.com"
                )
            )
        )
    })
}

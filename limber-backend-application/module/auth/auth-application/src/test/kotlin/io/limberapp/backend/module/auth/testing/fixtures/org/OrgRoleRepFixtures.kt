package io.limberapp.backend.module.auth.testing.fixtures.org

import io.limberapp.backend.authorization.permissions.OrgPermissions
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import java.time.LocalDateTime

internal object OrgRoleRepFixtures {
    data class Fixture(
        val creation: () -> OrgRoleRep.Creation,
        val complete: ResourceTest.(idSeed: Int) -> OrgRoleRep.Complete
    )

    val adminFixture = Fixture(
        {
            OrgRoleRep.Creation("Admin")
        },
        { idSeed ->
            OrgRoleRep.Complete(
                guid = deterministicUuidGenerator[idSeed],
                createdDate = LocalDateTime.now(fixedClock),
                name = "Admin",
                permissions = OrgPermissions.none()
            )
        }
    )

    val maintainerFixture = Fixture(
        {
            OrgRoleRep.Creation("Maintainer")
        },
        { idSeed ->
            OrgRoleRep.Complete(
                guid = deterministicUuidGenerator[idSeed],
                createdDate = LocalDateTime.now(fixedClock),
                name = "Maintainer",
                permissions = OrgPermissions.none()
            )
        }
    )

    val memberFixture = Fixture(
        {
            OrgRoleRep.Creation("Member")
        },
        { idSeed ->
            OrgRoleRep.Complete(
                guid = deterministicUuidGenerator[idSeed],
                createdDate = LocalDateTime.now(fixedClock),
                name = "Member",
                permissions = OrgPermissions.none()
            )
        }
    )
}

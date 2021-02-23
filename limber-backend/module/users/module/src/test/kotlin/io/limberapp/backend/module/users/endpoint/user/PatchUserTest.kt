package io.limberapp.backend.module.users.endpoint.user

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.testing.IntegrationTest
import io.limberapp.backend.module.users.testing.fixtures.user.UserRepFixtures
import io.limberapp.common.permissions.limberPermissions.LimberPermission
import io.limberapp.common.server.Server
import io.limberapp.common.util.string.fullName
import org.junit.jupiter.api.Test
import java.util.UUID

internal class PatchUserTest(
    engine: TestApplicationEngine,
    limberServer: Server<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun doesNotExist() {
    val userGuid = UUID.randomUUID()

    test(expectResult = null) {
      userClient(UserApi.Patch(userGuid, UserRep.Update(firstName = "Gunner")))
    }
  }

  @Test
  fun happyPathName() {
    val orgGuid = UUID.randomUUID()

    var userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    setup {
      userClient(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))
    }

    userRep = userRep.copy(
        firstName = "Gunner",
        fullName = fullName("Gunner", userRep.lastName)
    )
    test(expectResult = userRep) {
      userClient(UserApi.Patch(userRep.guid, UserRep.Update(firstName = "Gunner")))
    }

    test(expectResult = userRep) {
      userClient(UserApi.Get(userRep.guid))
    }
  }

  @Test
  fun happyPathRole() {
    val orgGuid = UUID.randomUUID()

    var userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    setup {
      userClient(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))
    }

    userRep = userRep.copy(permissions = userRep.permissions + LimberPermission.SUPERUSER)
    test(expectResult = userRep) {
      userClient(UserApi.Patch(userRep.guid,
          UserRep.Update(permissions = userRep.permissions.plus(LimberPermission.SUPERUSER))))
    }

    test(expectResult = userRep) {
      userClient(UserApi.Get(userRep.guid))
    }
  }
}

package io.limberapp.endpoint.user

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.user.UserApi
import io.limberapp.permissions.limber.LimberPermission
import io.limberapp.rep.user.UserRep
import io.limberapp.rep.user.UserRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import io.limberapp.util.string.fullName
import org.junit.jupiter.api.Test
import java.util.UUID

internal class PatchUserTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `user does not exist`() {
    val userGuid = UUID.randomUUID()

    test(expectResult = null) {
      userClient(UserApi.Patch(userGuid, UserRep.Update(firstName = "Gunner")))
    }
  }

  @Test
  fun `name changed`() {
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
  fun `permissions changed`() {
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

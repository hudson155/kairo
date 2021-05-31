package io.limberapp.endpoint.user

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.user.UserApi
import io.limberapp.permissions.limber.LimberPermission
import io.limberapp.rep.user.UserRep
import io.limberapp.rep.user.UserRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
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
      userClient(UserApi.Patch(userGuid, UserRep.Update(fullName = "Gunner Hudson")))
    }
  }

  @Test
  fun `name changed`() {
    val orgGuid = UUID.randomUUID()

    var userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    setup { userClient(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid))) }

    userRep = userRep.copy(fullName = "Gunner Hudson")
    test(expectResult = userRep) {
      userClient(UserApi.Patch(userRep.guid, UserRep.Update(fullName = "Gunner Hudson")))
    }

    test(expectResult = userRep) { userClient(UserApi.Get(userRep.guid)) }
  }

  @Test
  fun `permissions changed`() {
    val orgGuid = UUID.randomUUID()

    var userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    setup { userClient(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid))) }

    userRep = userRep.copy(permissions = userRep.permissions + LimberPermission.SUPERUSER)
    test(expectResult = userRep) {
      userClient(UserApi.Patch(
          userGuid = userRep.guid,
          rep = UserRep.Update(permissions = userRep.permissions.plus(LimberPermission.SUPERUSER)),
      ))
    }

    test(expectResult = userRep) { userClient(UserApi.Get(userRep.guid)) }
  }
}

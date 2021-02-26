package io.limberapp.endpoint.user

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.user.UserApi
import io.limberapp.rep.user.UserRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.Test
import java.util.UUID

internal class DeleteUserTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `user does not exist`() {
    val userGuid = UUID.randomUUID()

    test(expectResult = null) {
      userClient(UserApi.Delete(userGuid))
    }
  }

  @Test
  fun `user exists`() {
    val orgGuid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    setup {
      userClient(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))
    }

    test(expectResult = Unit) {
      userClient(UserApi.Delete(userRep.guid))
    }

    test(expectResult = null) {
      userClient(UserApi.Get(userRep.guid))
    }
  }
}

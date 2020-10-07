package io.limberapp.backend.module.users.endpoint.user

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.testing.IntegrationTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*

internal class GetUserTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun doesNotExist() {
    val userGuid = UUID.randomUUID()

    test(expectResult = null) {
      userClient(UserApi.Get(userGuid))
    }
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    setup {
      userClient(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))
    }

    test(expectResult = userRep) {
      userClient(UserApi.Get(userRep.guid))
    }
  }
}

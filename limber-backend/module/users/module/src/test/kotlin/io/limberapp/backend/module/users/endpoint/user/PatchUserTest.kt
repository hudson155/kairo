package io.limberapp.backend.module.users.endpoint.user

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.IntegrationTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PatchUserTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun doesNotExist() {
    val userGuid = UUID.randomUUID()

    test(
      endpoint = UserApi.Patch(userGuid, UserRep.Update(firstName = "Gunner")),
      expectedException = UserNotFound()
    )
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    var userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    setup(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))

    userRep = userRep.copy(firstName = "Gunner")
    test(UserApi.Patch(userRep.guid, UserRep.Update(firstName = "Gunner"))) {
      val actual = json.parse<UserRep.Complete>(responseContent)
      assertEquals(userRep, actual)
    }

    test(UserApi.Get(userRep.guid)) {
      val actual = json.parse<UserRep.Complete>(responseContent)
      assertEquals(userRep, actual)
    }
  }
}

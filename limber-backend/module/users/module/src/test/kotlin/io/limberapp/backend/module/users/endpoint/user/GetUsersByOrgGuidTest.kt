package io.limberapp.backend.module.users.endpoint.user

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.testing.IntegrationTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import io.limberapp.backend.module.users.testing.fixtures.account.summary
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*

internal class GetUsersByOrgGuidTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun happyPathNoneFound() {
    val orgGuid = UUID.randomUUID()

    test(expectResult = emptySet()) {
      userClient(UserApi.GetByOrgGuid(orgGuid))
    }
  }

  @Test
  fun happyPathMultipleFound() {
    val orgGuid = UUID.randomUUID()

    val jeffHudsonUserRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    setup {
      userClient(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))
    }

    val billGatesUserRep = UserRepFixtures.billGatesFixture.complete(this, orgGuid, 1)
    setup {
      userClient(UserApi.Post(UserRepFixtures.billGatesFixture.creation(orgGuid)))
    }

    test(expectResult = setOf(jeffHudsonUserRep.summary(), billGatesUserRep.summary())) {
      userClient(UserApi.GetByOrgGuid(orgGuid))
    }
  }
}

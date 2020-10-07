package io.limberapp.backend.module.users.endpoint.user

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.testing.IntegrationTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*

internal class GetUserByOrgGuidAndEmailAddressTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun emailAddressDoesNotExist() {
    val orgGuid = UUID.randomUUID()

    setup {
      userClient(UserApi.Post(UserRepFixtures.billGatesFixture.creation(orgGuid)))
    }

    test(expectResult = null) {
      userClient(UserApi.GetByOrgGuidAndEmailAddress(orgGuid, "jhudson@jhudson.ca"))
    }
  }

  @Test
  fun emailAddressExistsInDifferentOrg() {
    val org0Guid = UUID.randomUUID()
    val org1Guid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, org0Guid, 0)
    setup {
      userClient(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(org0Guid)))
    }

    test(expectResult = null) {
      userClient(UserApi.GetByOrgGuidAndEmailAddress(org1Guid, userRep.emailAddress))
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
      userClient(UserApi.GetByOrgGuidAndEmailAddress(orgGuid, userRep.emailAddress))
    }
  }
}

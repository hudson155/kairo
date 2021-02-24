package io.limberapp.endpoint.user

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.user.UserApi
import io.limberapp.rep.user.UserRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.Test
import java.util.UUID

internal class GetUserByOrgGuidAndEmailAddressTest(
    engine: TestApplicationEngine,
    limberServer: Server<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun emailAddressDoesNotExist() {
    val orgGuid = UUID.randomUUID()

    setup {
      userClient(UserApi.Post(UserRepFixtures.billGatesFixture.creation(orgGuid)))
    }

    test(expectResult = null) {
      userClient(UserApi.GetByOrgGuidAndEmailAddress(orgGuid, "jeff.hudson@limberapp.io"))
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

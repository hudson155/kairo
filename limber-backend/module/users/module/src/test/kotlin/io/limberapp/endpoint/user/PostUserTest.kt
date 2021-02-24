package io.limberapp.endpoint.user

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.user.UserApi
import io.limberapp.exception.user.EmailAddressAlreadyTaken
import io.limberapp.rep.user.UserRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.Test
import java.util.UUID

internal class PostUserTest(
    engine: TestApplicationEngine,
    limberServer: Server<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun duplicateEmailAddress() {
    val orgGuid = UUID.randomUUID()

    val jeffHudsonUserRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    setup {
      userClient(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))
    }

    test(expectError = EmailAddressAlreadyTaken()) {
      userClient(UserApi.Post(
          rep = UserRepFixtures.billGatesFixture.creation(orgGuid)
              .copy(emailAddress = jeffHudsonUserRep.emailAddress)
      ))
    }
  }

  @Test
  fun duplicateInDifferentOrg() {
    val org0Guid = UUID.randomUUID()
    val org1Guid = UUID.randomUUID()

    setup { userClient(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(org0Guid))) }

    val jeffHudsonUserRep = UserRepFixtures.jeffHudsonFixture.complete(this, org1Guid, 1)
    test(expectResult = jeffHudsonUserRep) {
      userClient(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(org1Guid)))
    }
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    test(expectResult = userRep) {
      userClient(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))
    }

    test(expectResult = userRep) {
      userClient(UserApi.Get(userRep.guid))
    }
  }
}

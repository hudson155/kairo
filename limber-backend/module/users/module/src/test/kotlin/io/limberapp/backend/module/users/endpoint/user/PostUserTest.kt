package io.limberapp.backend.module.users.endpoint.user

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.exception.account.EmailAddressAlreadyTaken
import io.limberapp.backend.module.users.testing.IntegrationTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*

internal class PostUserTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
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

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

internal class GetUserByOrgGuidAndEmailAddressTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun emailAddressDoesNotExist() {
    val orgGuid = UUID.randomUUID()

    test(UserApi.Post(UserRepFixtures.billGatesFixture.creation(orgGuid))) {}

    test(
      endpoint = UserApi.GetByOrgGuidAndEmailAddress(orgGuid, "jhudson@jhudson.ca"),
      expectedException = UserNotFound()
    )
  }

  @Test
  fun emailAddressExistsInDifferentOrg() {
    val org0Guid = UUID.randomUUID()
    val org1Guid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, org0Guid, 0)
    test(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(org0Guid))) {}

    test(
      endpoint = UserApi.GetByOrgGuidAndEmailAddress(org1Guid, userRep.emailAddress),
      expectedException = UserNotFound()
    )
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    test(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid))) {}

    test(UserApi.GetByOrgGuidAndEmailAddress(orgGuid, userRep.emailAddress)) {
      val actual = json.parse<UserRep.Complete>(responseContent)
      assertEquals(userRep, actual)
    }
  }
}

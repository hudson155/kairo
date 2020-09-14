package io.limberapp.backend.module.users.endpoint.user

import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import io.limberapp.common.testing.responseContent
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class GetUserByOrgGuidAndEmailAddressTest : ResourceTest() {
  @Test
  fun emailAddressDoesNotExist() {
    val orgGuid = UUID.randomUUID()

    limberTest.test(UserApi.Post(UserRepFixtures.billGatesFixture.creation(orgGuid))) {}

    limberTest.test(
      endpoint = UserApi.GetByOrgGuidAndEmailAddress(orgGuid, "jhudson@jhudson.ca"),
      expectedException = UserNotFound()
    )
  }

  @Test
  fun emailAddressExistsInDifferentOrg() {
    val org0Guid = UUID.randomUUID()
    val org1Guid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, org0Guid, 0)
    limberTest.test(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(org0Guid))) {}

    limberTest.test(
      endpoint = UserApi.GetByOrgGuidAndEmailAddress(org1Guid, userRep.emailAddress),
      expectedException = UserNotFound()
    )
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    limberTest.test(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid))) {}

    limberTest.test(UserApi.GetByOrgGuidAndEmailAddress(orgGuid, userRep.emailAddress)) {
      val actual = json.parse<UserRep.Complete>(responseContent)
      assertEquals(userRep, actual)
    }
  }
}

package io.limberapp.backend.module.users.endpoint.user

import com.piperframework.testing.responseContent
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class GetUserByOrgGuidAndEmailAddressTest : ResourceTest() {
  @Test
  fun emailAddressDoesNotExist() {
    val orgGuid = UUID.randomUUID()

    piperTest.test(UserApi.Post(UserRepFixtures.billGatesFixture.creation(orgGuid))) {}

    piperTest.test(
      endpoint = UserApi.GetByOrgGuidAndEmailAddress(orgGuid, "jhudson@jhudson.ca"),
      expectedException = UserNotFound()
    )
  }

  @Test
  fun emailAddressExistsInDifferentOrg() {
    val org0Guid = UUID.randomUUID()
    val org1Guid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, org0Guid, 0)
    piperTest.test(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(org0Guid))) {}

    piperTest.test(
      endpoint = UserApi.GetByOrgGuidAndEmailAddress(org1Guid, userRep.emailAddress),
      expectedException = UserNotFound()
    )
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    piperTest.test(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid))) {}

    piperTest.test(UserApi.GetByOrgGuidAndEmailAddress(orgGuid, userRep.emailAddress)) {
      val actual = json.parse<UserRep.Complete>(responseContent)
      assertEquals(userRep, actual)
    }
  }
}

package io.limberapp.backend.module.users.endpoint.user

import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.exception.account.EmailAddressAlreadyTaken
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import io.limberapp.common.testing.responseContent
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PostUserTest : ResourceTest() {
  @Test
  fun duplicateEmailAddress() {
    val orgGuid = UUID.randomUUID()

    val jeffHudsonUserRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    limberTest.setup(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))

    limberTest.test(
      endpoint = UserApi.Post(
        rep = UserRepFixtures.billGatesFixture.creation(orgGuid)
          .copy(emailAddress = jeffHudsonUserRep.emailAddress)
      ),
      expectedException = EmailAddressAlreadyTaken()
    )
  }

  @Test
  fun duplicateEmailAddressDifferentOrg() {
    val org0Guid = UUID.randomUUID()
    val org1Guid = UUID.randomUUID()

    val jeffHudsonUserRep = UserRepFixtures.jeffHudsonFixture.complete(this, org0Guid, 0)
    limberTest.setup(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(org0Guid)))

    // Just checking that this doesn't throw an exception. Not checking the response.
    limberTest.test(
      endpoint = UserApi.Post(
        rep = UserRepFixtures.billGatesFixture.creation(org1Guid)
          .copy(emailAddress = jeffHudsonUserRep.emailAddress)
      )
    ) {}
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    limberTest.test(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid))) {
      val actual = json.parse<UserRep.Complete>(responseContent)
      assertEquals(userRep, actual)
    }

    limberTest.test(UserApi.Get(userRep.guid)) {
      val actual = json.parse<UserRep.Complete>(responseContent)
      assertEquals(userRep, actual)
    }
  }
}

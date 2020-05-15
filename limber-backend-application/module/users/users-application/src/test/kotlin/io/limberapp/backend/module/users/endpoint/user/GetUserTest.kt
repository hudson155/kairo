package io.limberapp.backend.module.users.endpoint.user

import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class GetUserTest : ResourceTest() {
  @Test
  fun doesNotExist() {
    val userGuid = UUID.randomUUID()

    piperTest.test(
      endpoint = UserApi.Get(userGuid),
      expectedException = UserNotFound()
    )
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    piperTest.setup(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))

    piperTest.test(UserApi.Get(userRep.guid)) {
      val actual = json.parse<UserRep.Complete>(response.content!!)
      assertEquals(userRep, actual)
    }
  }
}

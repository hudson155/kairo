package io.limberapp.backend.module.users.endpoint.user

import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PatchUserTest : ResourceTest() {
  @Test
  fun doesNotExist() {
    val userGuid = UUID.randomUUID()

    piperTest.test(
      endpoint = UserApi.Patch(userGuid, UserRep.Update(firstName = "Gunner")),
      expectedException = UserNotFound()
    )
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    var userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    piperTest.setup(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))

    userRep = userRep.copy(firstName = "Gunner")
    piperTest.test(UserApi.Patch(userRep.guid, UserRep.Update(firstName = "Gunner"))) {
      val actual = json.parse<UserRep.Complete>(response.content!!)
      assertEquals(userRep, actual)
    }

    piperTest.test(UserApi.Get(userRep.guid)) {
      val actual = json.parse<UserRep.Complete>(response.content!!)
      assertEquals(userRep, actual)
    }
  }
}

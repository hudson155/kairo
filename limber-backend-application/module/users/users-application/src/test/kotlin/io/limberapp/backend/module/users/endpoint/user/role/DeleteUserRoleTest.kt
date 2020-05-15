package io.limberapp.backend.module.users.endpoint.user.role

import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.api.user.role.UserRoleApi
import io.limberapp.backend.module.users.exception.account.UserDoesNotHaveRole
import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class DeleteUserRoleTest : ResourceTest() {
  @Test
  fun userDoesNotExist() {
    val userGuid = UUID.randomUUID()

    piperTest.test(
      endpoint = UserRoleApi.Delete(userGuid, JwtRole.SUPERUSER),
      expectedException = UserNotFound()
    )
  }

  @Test
  fun roleDoesNotExist() {
    val orgGuid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    piperTest.setup(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))

    piperTest.test(
      endpoint = UserRoleApi.Delete(userRep.guid, JwtRole.SUPERUSER),
      expectedException = UserDoesNotHaveRole()
    )

    piperTest.test(UserApi.Get(userRep.guid)) {
      val actual = json.parse<UserRep.Complete>(response.content!!)
      assertEquals(userRep, actual)
    }
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    var userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    piperTest.setup(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))

    userRep = userRep.copy(roles = userRep.roles.plus(JwtRole.SUPERUSER))
    piperTest.setup(UserRoleApi.Put(userRep.guid, JwtRole.SUPERUSER))

    userRep = userRep.copy(roles = userRep.roles.filter { it != JwtRole.SUPERUSER }.toSet())
    piperTest.test(UserRoleApi.Delete(userRep.guid, JwtRole.SUPERUSER)) {}

    piperTest.test(UserApi.Get(userRep.guid)) {
      val actual = json.parse<UserRep.Complete>(response.content!!)
      assertEquals(userRep, actual)
    }
  }
}

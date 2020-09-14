package io.limberapp.backend.module.users.endpoint.user.role

import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.api.user.role.UserRoleApi
import io.limberapp.backend.module.users.exception.account.UserDoesNotHaveRole
import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import io.limberapp.common.testing.responseContent
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class DeleteUserRoleTest : ResourceTest() {
  @Test
  fun userDoesNotExist() {
    val userGuid = UUID.randomUUID()

    limberTest.test(
      endpoint = UserRoleApi.Delete(userGuid, JwtRole.SUPERUSER),
      expectedException = UserNotFound()
    )
  }

  @Test
  fun roleDoesNotExist() {
    val orgGuid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    limberTest.setup(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))

    limberTest.test(
      endpoint = UserRoleApi.Delete(userRep.guid, JwtRole.SUPERUSER),
      expectedException = UserDoesNotHaveRole()
    )

    limberTest.test(UserApi.Get(userRep.guid)) {
      val actual = json.parse<UserRep.Complete>(responseContent)
      assertEquals(userRep, actual)
    }
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    var userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    limberTest.setup(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))

    userRep = userRep.copy(roles = userRep.roles + JwtRole.SUPERUSER)
    limberTest.setup(UserRoleApi.Put(userRep.guid, JwtRole.SUPERUSER))

    userRep = userRep.copy(roles = userRep.roles.filter { it != JwtRole.SUPERUSER }.toSet())
    limberTest.test(UserRoleApi.Delete(userRep.guid, JwtRole.SUPERUSER)) {}

    limberTest.test(UserApi.Get(userRep.guid)) {
      val actual = json.parse<UserRep.Complete>(responseContent)
      assertEquals(userRep, actual)
    }
  }
}

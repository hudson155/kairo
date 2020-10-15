package io.limberapp.backend.module.users.endpoint.user.role

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.users.api.account.UserApi
import io.limberapp.backend.module.users.api.account.UserRoleApi
import io.limberapp.backend.module.users.exception.account.UserDoesNotHaveRole
import io.limberapp.backend.module.users.testing.IntegrationTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import io.limberapp.common.LimberApplication
import io.limberapp.permissions.AccountRole
import org.junit.jupiter.api.Test
import java.util.*

internal class DeleteUserRoleTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun userDoesNotExist() {
    val userGuid = UUID.randomUUID()

    test(expectResult = null) {
      userRoleClient(UserRoleApi.Delete(userGuid, AccountRole.SUPERUSER))
    }
  }

  @Test
  fun roleDoesNotExist() {
    val orgGuid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    setup {
      userClient(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))
    }

    test(expectError = UserDoesNotHaveRole()) {
      userRoleClient(UserRoleApi.Delete(userRep.guid, AccountRole.SUPERUSER))
    }

    test(expectResult = userRep) {
      userClient(UserApi.Get(userRep.guid))
    }
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    var userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    setup {
      userClient(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))
    }

    userRep = userRep.copy(roles = userRep.roles + AccountRole.SUPERUSER)
    setup {
      userRoleClient(UserRoleApi.Put(userRep.guid, AccountRole.SUPERUSER))
    }

    userRep = userRep.copy(roles = userRep.roles.filter { it != AccountRole.SUPERUSER }.toSet())
    test(expectResult = Unit) {
      userRoleClient(UserRoleApi.Delete(userRep.guid, AccountRole.SUPERUSER))
    }

    test(expectResult = userRep) {
      userClient(UserApi.Get(userRep.guid))
    }
  }
}

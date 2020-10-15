package io.limberapp.backend.module.users.endpoint.user.role

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.users.api.account.UserApi
import io.limberapp.backend.module.users.api.account.UserRoleApi
import io.limberapp.backend.module.users.testing.IntegrationTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import io.limberapp.common.LimberApplication
import io.limberapp.permissions.AccountRole
import org.junit.jupiter.api.Test
import java.util.*

internal class PutUserRoleTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun userDoesNotExist() {
    val userGuid = UUID.randomUUID()

    test(expectResult = null) {
      userRoleClient(UserRoleApi.Put(userGuid, AccountRole.SUPERUSER))
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
    test(expectResult = Unit) {
      userRoleClient(UserRoleApi.Put(userRep.guid, AccountRole.SUPERUSER))
    }

    test(expectResult = userRep) {
      userClient(UserApi.Get(userRep.guid))
    }
  }

  @Test
  fun happyPathIdempotent() {
    val orgGuid = UUID.randomUUID()

    var userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    setup {
      userClient(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))
    }

    userRep = userRep.copy(roles = userRep.roles + AccountRole.SUPERUSER)
    setup {
      userRoleClient(UserRoleApi.Put(userRep.guid, AccountRole.SUPERUSER))
    }

    test(expectResult = Unit) {
      userRoleClient(UserRoleApi.Put(userRep.guid, AccountRole.SUPERUSER))
    }

    test(expectResult = userRep) {
      userClient(UserApi.Get(userRep.guid))
    }
  }
}

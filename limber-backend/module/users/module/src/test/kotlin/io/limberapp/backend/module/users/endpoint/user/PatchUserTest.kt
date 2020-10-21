package io.limberapp.backend.module.users.endpoint.user

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.users.api.account.UserApi
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.IntegrationTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import io.limberapp.common.LimberApplication
import io.limberapp.common.permissions.AccountRole
import io.limberapp.common.util.string.joinNames
import org.junit.jupiter.api.Test
import java.util.*

internal class PatchUserTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun doesNotExist() {
    val userGuid = UUID.randomUUID()

    test(expectResult = null) {
      userClient(UserApi.Patch(userGuid, UserRep.Update(firstName = "Gunner")))
    }
  }

  @Test
  fun happyPathName() {
    val orgGuid = UUID.randomUUID()

    var userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    setup {
      userClient(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))
    }

    userRep = userRep.copy(firstName = "Gunner", fullName = listOfNotNull("Gunner", userRep.lastName).joinNames())
    test(expectResult = userRep) {
      userClient(UserApi.Patch(userRep.guid, UserRep.Update(firstName = "Gunner")))
    }

    test(expectResult = userRep) {
      userClient(UserApi.Get(userRep.guid))
    }
  }

  @Test
  fun happyPathRole() {
    val orgGuid = UUID.randomUUID()

    var userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    setup {
      userClient(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))
    }

    userRep = userRep.copy(roles = userRep.roles + AccountRole.SUPERUSER)
    test(expectResult = userRep) {
      userClient(UserApi.Patch(userRep.guid, UserRep.Update(superuser = true)))
    }

    test(expectResult = userRep) {
      userClient(UserApi.Get(userRep.guid))
    }
  }
}

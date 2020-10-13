package io.limberapp.backend.module.users.endpoint.user

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.users.api.account.UserApi
import io.limberapp.backend.module.users.exception.account.CannotDeleteOrgOwner
import io.limberapp.backend.module.users.testing.IntegrationTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import io.limberapp.common.LimberApplication
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.*

internal class DeleteUserTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun doesNotExist() {
    val userGuid = UUID.randomUUID()

    every { mocks[OrgService::class].getByOwnerUserGuid(userGuid) } returns null

    test(expectResult = null) {
      userClient(UserApi.Delete(userGuid))
    }
  }

  @Test
  fun userIsOwnerOfOrg() {
    val orgGuid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    setup {
      userClient(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))
    }

    every { mocks[OrgService::class].getByOwnerUserGuid(userRep.guid) } returns mockk()

    test(expectError = CannotDeleteOrgOwner()) {
      userClient(UserApi.Delete(userRep.guid))
    }

    test(expectResult = userRep) {
      userClient(UserApi.Get(userRep.guid))
    }
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    setup {
      userClient(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))
    }

    every { mocks[OrgService::class].getByOwnerUserGuid(userRep.guid) } returns null

    test(expectResult = Unit) {
      userClient(UserApi.Delete(userRep.guid))
    }

    test(expectResult = null) {
      userClient(UserApi.Get(userRep.guid))
    }
  }
}

package io.limberapp.backend.module.users.endpoint.user

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.client.org.OrgClient
import io.limberapp.backend.module.users.api.account.UserApi
import io.limberapp.backend.module.users.exception.account.CannotDeleteOrgOwner
import io.limberapp.backend.module.users.testing.IntegrationTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import io.limberapp.common.LimberApplication
import io.mockk.coEvery
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

    coEvery { mocks[OrgClient::class](OrgApi.GetByOwnerUserGuid(userGuid)) } returns null

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

    coEvery { mocks[OrgClient::class](OrgApi.GetByOwnerUserGuid(userRep.guid)) } returns mockk()

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

    coEvery { mocks[OrgClient::class](OrgApi.GetByOwnerUserGuid(userRep.guid)) } returns null

    test(expectResult = Unit) {
      userClient(UserApi.Delete(userRep.guid))
    }

    test(expectResult = null) {
      userClient(UserApi.Get(userRep.guid))
    }
  }
}

package io.limberapp.backend.module.users.endpoint.user

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.exception.account.CannotDeleteOrgOwner
import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.IntegrationTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import io.limberapp.common.LimberApplication
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class DeleteUserTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun doesNotExist() {
    val userGuid = UUID.randomUUID()

    every { mocks[OrgService::class].getByOwnerUserGuid(userGuid) } returns null

    test(UserApi.Delete(userGuid), expectedException = UserNotFound())
  }

  @Test
  fun userIsOwnerOfOrg() {
    val orgGuid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    setup(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))

    every { mocks[OrgService::class].getByOwnerUserGuid(userRep.guid) } returns mockk()

    test(UserApi.Delete(userRep.guid), expectedException = CannotDeleteOrgOwner())

    test(UserApi.Get(userRep.guid)) {
      val actual = json.parse<UserRep.Complete>(responseContent)
      assertEquals(userRep, actual)
    }
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    setup(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))

    every { mocks[OrgService::class].getByOwnerUserGuid(userRep.guid) } returns null

    test(UserApi.Delete(userRep.guid)) {}

    test(UserApi.Get(userRep.guid), expectedException = UserNotFound())
  }
}

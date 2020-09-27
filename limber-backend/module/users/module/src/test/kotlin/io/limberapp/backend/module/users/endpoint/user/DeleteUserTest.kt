package io.limberapp.backend.module.users.endpoint.user

import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.exception.account.CannotDeleteOrgOwner
import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import io.limberapp.common.testing.responseContent
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class DeleteUserTest : ResourceTest() {
  @Test
  fun doesNotExist() {
    val userGuid = UUID.randomUUID()

    every { mockedServices[OrgService::class].getByOwnerUserGuid(userGuid) } returns null

    limberTest.test(
      endpoint = UserApi.Delete(userGuid),
      expectedException = UserNotFound()
    )
  }

  @Test
  fun userIsOwnerOfOrg() {
    val orgGuid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    limberTest.setup(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))

    every { mockedServices[OrgService::class].getByOwnerUserGuid(userRep.guid) } returns mockk()

    limberTest.test(
      endpoint = UserApi.Delete(userRep.guid),
      expectedException = CannotDeleteOrgOwner()
    )

    limberTest.test(UserApi.Get(userRep.guid)) {
      val actual = json.parse<UserRep.Complete>(responseContent)
      assertEquals(userRep, actual)
    }
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    limberTest.setup(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))

    every { mockedServices[OrgService::class].getByOwnerUserGuid(userRep.guid) } returns null

    limberTest.test(UserApi.Delete(userRep.guid)) {}

    limberTest.test(
      endpoint = UserApi.Get(userRep.guid),
      expectedException = UserNotFound()
    )
  }
}

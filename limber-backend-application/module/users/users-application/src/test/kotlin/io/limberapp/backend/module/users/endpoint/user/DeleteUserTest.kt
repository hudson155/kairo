package io.limberapp.backend.module.users.endpoint.user

import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.exception.account.CannotDeleteOrgOwner
import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class DeleteUserTest : ResourceTest() {
  @Test
  fun doesNotExist() {
    val userGuid = UUID.randomUUID()

    every { mockedServices[OrgService::class].getByOwnerAccountGuid(userGuid) } returns null

    piperTest.test(
      endpoint = UserApi.Delete(userGuid),
      expectedException = UserNotFound()
    )
  }

  @Test
  fun userIsOwnerOfOrg() {
    val orgGuid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    piperTest.setup(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))

    every { mockedServices[OrgService::class].getByOwnerAccountGuid(userRep.guid) } returns mockk()

    piperTest.test(
      endpoint = UserApi.Delete(userRep.guid),
      expectedException = CannotDeleteOrgOwner()
    )

    piperTest.test(UserApi.Get(userRep.guid)) {
      val actual = json.parse<UserRep.Complete>(response.content!!)
      assertEquals(userRep, actual)
    }
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    piperTest.setup(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))

    every { mockedServices[OrgService::class].getByOwnerAccountGuid(userRep.guid) } returns null

    piperTest.test(UserApi.Delete(userRep.guid)) {}

    piperTest.test(
      endpoint = UserApi.Get(userRep.guid),
      expectedException = UserNotFound()
    )
  }
}

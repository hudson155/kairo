package io.limberapp.backend.module.users.endpoint.user

import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import io.limberapp.backend.module.users.testing.fixtures.account.summary
import io.limberapp.common.testing.responseContent
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetUsersByOrgGuidTest : ResourceTest() {
  @Test
  fun happyPathNoneFound() {
    val orgGuid = UUID.randomUUID()

    limberTest.test(UserApi.GetByOrgGuid(orgGuid)) {
      val actual = json.parseSet<UserRep.Summary>(responseContent)
      assertTrue(actual.isEmpty())
    }
  }

  @Test
  fun happyPathMultipleFound() {
    val orgGuid = UUID.randomUUID()

    val jeffHudsonUserRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    limberTest.test(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid))) {}

    val billGatesUserRep = UserRepFixtures.billGatesFixture.complete(this, orgGuid, 1)
    limberTest.test(UserApi.Post(UserRepFixtures.billGatesFixture.creation(orgGuid))) {}

    limberTest.test(UserApi.GetByOrgGuid(orgGuid)) {
      val actual = json.parseSet<UserRep.Summary>(responseContent)
      assertEquals(setOf(jeffHudsonUserRep.summary(), billGatesUserRep.summary()), actual)
    }
  }
}

package io.limberapp.backend.module.users.endpoint.user

import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import io.limberapp.backend.module.users.testing.fixtures.account.summary
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetUsersByOrgGuidTest : ResourceTest() {
    @Test
    fun happyPathNoneFound() {
        val orgGuid = UUID.randomUUID()

        piperTest.test(UserApi.GetByOrgGuid(orgGuid)) {
            val actual = json.parseSet<UserRep.Summary>(response.content!!)
            assertTrue(actual.isEmpty())
        }
    }

    @Test
    fun happyPathMultipleFound() {
        val orgGuid = UUID.randomUUID()

        val jeffHudsonUserRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
        piperTest.test(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid))) {}

        val billGatesUserRep = UserRepFixtures.billGatesFixture.complete(this, orgGuid, 1)
        piperTest.test(UserApi.Post(UserRepFixtures.billGatesFixture.creation(orgGuid))) {}

        piperTest.test(UserApi.GetByOrgGuid(orgGuid)) {
            val actual = json.parseSet<UserRep.Summary>(response.content!!)
            assertEquals(setOf(jeffHudsonUserRep.summary(), billGatesUserRep.summary()), actual)
        }
    }
}

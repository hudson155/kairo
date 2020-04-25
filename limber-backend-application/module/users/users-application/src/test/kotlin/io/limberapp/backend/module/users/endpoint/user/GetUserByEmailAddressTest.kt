package io.limberapp.backend.module.users.endpoint.user

import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class GetUserByEmailAddressTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        val emailAddress = "jhudson@jhudson.ca"

        piperTest.test(
            endpoint = UserApi.GetByEmailAddress(emailAddress),
            expectedException = UserNotFound()
        )
    }

    @Test
    fun happyPath() {

        val orgId = UUID.randomUUID()

        val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgId, 0)
        piperTest.test(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgId))) {}

        piperTest.test(UserApi.GetByEmailAddress(userRep.emailAddress)) {
            val actual = json.parse<UserRep.Complete>(response.content!!)
            assertEquals(userRep, actual)
        }
    }
}

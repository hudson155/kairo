package io.limberapp.backend.module.users.endpoint.user

import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.user.UserRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class GetUserByEmailAddressTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        val emailAddress = "jhudson@jhudson.ca"

        piperTest.test(
            endpointConfig = GetUserByEmailAddress.endpointConfig,
            queryParams = mapOf(GetUserByEmailAddress.emailAddress to emailAddress),
            expectedException = UserNotFound()
        )
    }

    @Test
    fun happyPath() {

        val orgId = UUID.randomUUID()

        val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgId, 0)
        piperTest.test(
            endpointConfig = PostUser.endpointConfig,
            body = UserRepFixtures.jeffHudsonFixture.creation(orgId)
        ) {}

        piperTest.test(
            endpointConfig = GetUserByEmailAddress.endpointConfig,
            queryParams = mapOf(GetUserByEmailAddress.emailAddress to userRep.emailAddress)
        ) {
            val actual = json.parse<UserRep.Complete>(response.content!!)
            assertEquals(userRep, actual)
        }
    }
}

package io.limberapp.backend.module.users.endpoint.user

import io.limberapp.backend.module.users.exception.account.EmailAddressAlreadyTaken
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.user.UserRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class PostUserTest : ResourceTest() {

    @Test
    fun duplicateEmailAddress() {

        val orgId = UUID.randomUUID()

        val jeffHudsonUserRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = PostUser.endpointConfig,
            body = UserRepFixtures.jeffHudsonFixture.creation(orgId)
        )

        piperTest.test(
            endpointConfig = PostUser.endpointConfig,
            body = UserRepFixtures.billGatesFixture.creation(orgId).copy(emailAddress = jeffHudsonUserRep.emailAddress),
            expectedException = EmailAddressAlreadyTaken(jeffHudsonUserRep.emailAddress)
        )
    }

    @Test
    fun happyPath() {

        val orgId = UUID.randomUUID()

        val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgId, 0)
        piperTest.test(
            endpointConfig = PostUser.endpointConfig,
            body = UserRepFixtures.jeffHudsonFixture.creation(orgId)
        ) {
            val actual = json.parse<UserRep.Complete>(response.content!!)
            assertEquals(userRep, actual)
        }

        piperTest.test(
            endpointConfig = GetUser.endpointConfig,
            pathParams = mapOf(GetUser.userId to userRep.id)
        ) {
            val actual = json.parse<UserRep.Complete>(response.content!!)
            assertEquals(userRep, actual)
        }
    }
}

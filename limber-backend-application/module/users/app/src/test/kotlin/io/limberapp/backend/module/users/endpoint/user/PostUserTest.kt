package io.limberapp.backend.module.users.endpoint.user

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.users.exception.account.EmailAddressAlreadyTaken
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.user.UserRepFixtures
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class PostUserTest : ResourceTest() {

    @Test
    fun duplicateEmailAddress() {

        // PostUser
        val jeffHudsonUserRep = UserRepFixtures.jeffHudsonFixture.complete(this, 0)
        piperTest.setup(
            endpointConfig = PostUser.endpointConfig,
            body = UserRepFixtures.jeffHudsonFixture.creation()
        )

        // PostUser
        piperTest.test(
            endpointConfig = PostUser.endpointConfig,
            body = UserRepFixtures.billGatesFixture.creation().copy(emailAddress = jeffHudsonUserRep.emailAddress),
            expectedException = EmailAddressAlreadyTaken(jeffHudsonUserRep.emailAddress)
        )
    }

    @Test
    fun happyPath() {

        // PostUser
        val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, 0)
        piperTest.test(
            endpointConfig = PostUser.endpointConfig,
            body = UserRepFixtures.jeffHudsonFixture.creation()
        ) {
            val actual = objectMapper.readValue<UserRep.Complete>(response.content!!)
            assertEquals(userRep, actual)
        }

        // GetUser
        piperTest.test(
            endpointConfig = GetUser.endpointConfig,
            pathParams = mapOf(GetUser.userId to userRep.id)
        ) {
            val actual = objectMapper.readValue<UserRep.Complete>(response.content!!)
            assertEquals(userRep, actual)
        }
    }
}

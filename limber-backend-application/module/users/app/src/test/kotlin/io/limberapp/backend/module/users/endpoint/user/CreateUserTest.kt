package io.limberapp.backend.module.users.endpoint.user

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.users.exception.account.EmailAddressAlreadyTaken
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.user.UserRepFixtures
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class CreateUserTest : ResourceTest() {

    @Test
    fun duplicateEmailAddress() {

        // CreateUser
        piperTest.setup(
            endpointConfig = CreateUser.endpointConfig,
            body = UserRepFixtures.jeffHudsonFixture.creation()
        )

        // CreateUser
        piperTest.test(
            endpointConfig = CreateUser.endpointConfig,
            body = UserRepFixtures.billGatesFixture.creation().copy(
                emailAddress = UserRepFixtures.jeffHudsonFixture.creation().emailAddress
            ),
            expectedException = EmailAddressAlreadyTaken(
                UserRepFixtures.jeffHudsonFixture.creation().emailAddress
            )
        )
    }

    @Test
    fun happyPath() {

        // CreateUser
        val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, 0)
        piperTest.test(
            endpointConfig = CreateUser.endpointConfig,
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

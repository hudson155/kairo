package io.limberapp.backend.module.users.endpoint.user

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.users.exception.conflict.EmailAddressAlreadyTaken
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.user.UserRepFixtures
import org.junit.Test
import kotlin.test.assertEquals

internal class CreateUserTest : ResourceTest() {

    @Test
    fun duplicateEmailAddress() {

        // CreateUser
        piperTest.setup(
            endpointConfig = CreateUser.endpointConfig,
            body = UserRepFixtures[0].creation()
        )

        // CreateUser
        piperTest.test(
            endpointConfig = CreateUser.endpointConfig,
            body = UserRepFixtures[1].creation().copy(emailAddress = UserRepFixtures[0].creation().emailAddress),
            expectedException = EmailAddressAlreadyTaken(UserRepFixtures[0].creation().emailAddress)
        )
    }

    @Test
    fun happyPath() {

        // CreateUser
        val userRep = UserRepFixtures[0].complete(this, 0)
        piperTest.test(
            endpointConfig = CreateUser.endpointConfig,
            body = UserRepFixtures[0].creation()
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

package io.limberapp.backend.module.users.endpoint.user

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.users.exception.conflict.ConflictsWithAnotherUser
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.user.UserRepFixtures
import org.junit.Test
import kotlin.test.assertEquals

internal class CreateUserTest : ResourceTest() {

    @Test
    fun duplicateEmailAddress() {

        // CreateUser
        piperTest.test(
            endpointConfig = CreateUser.endpointConfig,
            body = UserRepFixtures.Creation[0]
        ) {}

        // CreateUser
        piperTest.test(
            endpointConfig = CreateUser.endpointConfig,
            body = UserRepFixtures.Creation[1].copy(emailAddress = UserRepFixtures.Creation[0].emailAddress),
            expectedException = ConflictsWithAnotherUser()
        )
    }

    @Test
    fun happyPath() {

        // CreateUser
        val userRep = UserRepFixtures.Complete[0](0)
        piperTest.test(
            endpointConfig = CreateUser.endpointConfig,
            body = UserRepFixtures.Creation[0]
        ) {
            val actual = objectMapper.readValue<UserRep.Complete>(response.content!!)
            assertEquals(userRep, actual)
        }

        // GetUser
        piperTest.test(
            endpointConfig = GetUser.endpointConfig,
            pathParams = mapOf(GetUser.userId to userRep.id.toString())
        ) {
            val actual = objectMapper.readValue<UserRep.Complete>(response.content!!)
            assertEquals(userRep, actual)
        }
    }
}

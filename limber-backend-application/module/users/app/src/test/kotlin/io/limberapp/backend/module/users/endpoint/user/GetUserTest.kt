package io.limberapp.backend.module.users.endpoint.user

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.users.exception.notFound.UserNotFound
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.user.UserRepFixtures
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class GetUserTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        // Setup
        val userId = UUID.randomUUID()

        // GetUser
        piperTest.test(
            endpointConfig = GetUser.endpointConfig,
            pathParams = mapOf(GetUser.userId to userId),
            expectedException = UserNotFound()
        )
    }

    @Test
    fun happyPath() {

        // CreateUser
        val userRep = UserRepFixtures[0].complete(this, 0)
        piperTest.setup(
            endpointConfig = CreateUser.endpointConfig,
            body = UserRepFixtures[0].creation()
        )

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

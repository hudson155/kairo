package io.limberapp.backend.module.users.endpoint.user

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.users.exception.notFound.UserNotFound
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.user.UserRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class UpdateUserTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        // Setup
        val userId = UUID.randomUUID()

        // UpdateUser
        val updateRep = UserRep.Update(firstName = "Gunner")
        piperTest.test(
            endpointConfig = UpdateUser.endpointConfig,
            pathParams = mapOf(UpdateUser.userId to userId),
            body = updateRep,
            expectedException = UserNotFound()
        )
    }

    @Test
    fun happyPath() {

        // CreateUser
        var userRep = UserRepFixtures[0].complete(this, 0)
        piperTest.setup(
            endpointConfig = CreateUser.endpointConfig,
            body = UserRepFixtures[0].creation()
        )

        // UpdateUser
        val updateRep = UserRep.Update(firstName = "Gunner")
        userRep = userRep.copy(firstName = updateRep.firstName!!)
        piperTest.test(
            endpointConfig = UpdateUser.endpointConfig,
            pathParams = mapOf(UpdateUser.userId to userRep.id),
            body = updateRep
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

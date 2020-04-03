package io.limberapp.backend.module.users.endpoint.user

import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.user.UserRepFixtures
import kotlinx.serialization.parse
import kotlinx.serialization.stringify
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class PatchUserTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        // Setup
        val userId = UUID.randomUUID()

        // PatchUser
        val updateRep = UserRep.Update(firstName = "Gunner")
        piperTest.test(
            endpointConfig = PatchUser.endpointConfig,
            pathParams = mapOf(PatchUser.userId to userId),
            body = json.stringify(updateRep),
            expectedException = UserNotFound()
        )
    }

    @Test
    fun happyPath() {

        // Setup
        val orgId = UUID.randomUUID()

        // PostUser
        var userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = PostUser.endpointConfig,
            body = json.stringify(UserRepFixtures.jeffHudsonFixture.creation(orgId))
        )

        // PatchUser
        val updateRep = UserRep.Update(firstName = "Gunner")
        userRep = userRep.copy(firstName = updateRep.firstName!!)
        piperTest.test(
            endpointConfig = PatchUser.endpointConfig,
            pathParams = mapOf(PatchUser.userId to userRep.id),
            body = json.stringify(updateRep)
        ) {
            val actual = json.parse<UserRep.Complete>(response.content!!)
            assertEquals(userRep, actual)
        }

        // GetUser
        piperTest.test(
            endpointConfig = GetUser.endpointConfig,
            pathParams = mapOf(GetUser.userId to userRep.id)
        ) {
            val actual = json.parse<UserRep.Complete>(response.content!!)
            assertEquals(userRep, actual)
        }
    }
}

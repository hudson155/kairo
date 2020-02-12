package io.limberapp.backend.module.users.endpoint.user

import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.user.UserRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID

internal class DeleteUserTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        // Setup
        val userId = UUID.randomUUID()

        // DeleteUser
        piperTest.test(
            endpointConfig = DeleteUser.endpointConfig,
            pathParams = mapOf(DeleteUser.userId to userId),
            expectedException = UserNotFound()
        )
    }

    @Test
    fun happyPath() {

        // PostUser
        val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, 0)
        piperTest.setup(
            endpointConfig = PostUser.endpointConfig,
            body = UserRepFixtures.jeffHudsonFixture.creation()
        )

        // DeleteUser
        piperTest.test(
            endpointConfig = DeleteUser.endpointConfig,
            pathParams = mapOf(DeleteUser.userId to userRep.id)
        ) {}

        // GetUser
        piperTest.test(
            endpointConfig = GetUser.endpointConfig,
            pathParams = mapOf(GetUser.userId to userRep.id),
            expectedException = UserNotFound()
        )
    }
}

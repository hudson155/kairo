package io.limberapp.backend.module.users.endpoint.user

import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.user.UserRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID

internal class DeleteUserTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        val userId = UUID.randomUUID()

        piperTest.test(
            endpointConfig = DeleteUser.endpointConfig,
            pathParams = mapOf(DeleteUser.userId to userId),
            expectedException = UserNotFound()
        )
    }

    @Test
    fun happyPath() {

        val orgId = UUID.randomUUID()

        val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = PostUser.endpointConfig,
            body = UserRepFixtures.jeffHudsonFixture.creation(orgId)
        )

        piperTest.test(
            endpointConfig = DeleteUser.endpointConfig,
            pathParams = mapOf(DeleteUser.userId to userRep.id)
        ) {}

        piperTest.test(
            endpointConfig = GetUser.endpointConfig,
            pathParams = mapOf(GetUser.userId to userRep.id),
            expectedException = UserNotFound()
        )
    }
}

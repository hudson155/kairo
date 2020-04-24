package io.limberapp.backend.module.users.endpoint.user

import io.limberapp.backend.module.users.api.user.UserApi
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
            endpoint = UserApi.Delete(userId),
            expectedException = UserNotFound()
        )
    }

    @Test
    fun happyPath() {
        val orgId = UUID.randomUUID()

        val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgId, 0)
        piperTest.setup(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgId)))

        piperTest.test(UserApi.Delete(userRep.id)) {}

        piperTest.test(
            endpoint = UserApi.Get(userRep.id),
            expectedException = UserNotFound()
        )
    }
}

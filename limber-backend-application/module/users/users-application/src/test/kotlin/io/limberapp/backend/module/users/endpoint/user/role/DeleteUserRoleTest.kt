package io.limberapp.backend.module.users.endpoint.user.role

import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.api.user.role.UserRoleApi
import io.limberapp.backend.module.users.exception.account.UserDoesNotHaveRole
import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class DeleteUserRoleTest : ResourceTest() {

    @Test
    fun userDoesNotExist() {

        val userId = UUID.randomUUID()

        piperTest.test(
            endpoint = UserRoleApi.Delete(userId, JwtRole.SUPERUSER),
            expectedException = UserNotFound()
        )
    }

    @Test
    fun roleDoesNotExist() {

        val orgId = UUID.randomUUID()

        val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgId, 0)
        piperTest.setup(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgId)))

        piperTest.test(
            endpoint = UserRoleApi.Delete(userRep.id, JwtRole.SUPERUSER),
            expectedException = UserDoesNotHaveRole(JwtRole.SUPERUSER)
        )

        piperTest.test(UserApi.Get(userRep.id)) {
            val actual = json.parse<UserRep.Complete>(response.content!!)
            assertEquals(userRep, actual)
        }
    }

    @Test
    fun happyPath() {

        val orgId = UUID.randomUUID()

        var userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgId, 0)
        piperTest.setup(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgId)))

        userRep = userRep.copy(roles = userRep.roles.plus(JwtRole.SUPERUSER))
        piperTest.setup(UserRoleApi.Put(userRep.id, JwtRole.SUPERUSER))

        userRep = userRep.copy(roles = userRep.roles.filter { it != JwtRole.SUPERUSER })
        piperTest.test(UserRoleApi.Delete(userRep.id, JwtRole.SUPERUSER)) {}

        piperTest.test(UserApi.Get(userRep.id)) {
            val actual = json.parse<UserRep.Complete>(response.content!!)
            assertEquals(userRep, actual)
        }
    }
}

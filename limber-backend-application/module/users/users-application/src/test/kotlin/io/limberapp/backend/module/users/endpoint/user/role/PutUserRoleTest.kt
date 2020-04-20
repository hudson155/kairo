package io.limberapp.backend.module.users.endpoint.user.role

import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.endpoint.user.GetUser
import io.limberapp.backend.module.users.endpoint.user.PostUser
import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.user.UserRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class PutUserRoleTest : ResourceTest() {

    @Test
    fun userDoesNotExist() {

        val userId = UUID.randomUUID()

        piperTest.test(
            endpointConfig = PutUserRole.endpointConfig,
            pathParams = mapOf(
                PutUserRole.userId to userId,
                PutUserRole.roleName to JwtRole.SUPERUSER
            ),
            expectedException = UserNotFound()
        )
    }

    @Test
    fun happyPath() {

        val orgId = UUID.randomUUID()

        var userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = PostUser.endpointConfig,
            body = UserRepFixtures.jeffHudsonFixture.creation(orgId)
        )

        userRep = userRep.copy(roles = userRep.roles.plus(JwtRole.SUPERUSER))
        piperTest.test(
            endpointConfig = PutUserRole.endpointConfig,
            pathParams = mapOf(
                PutUserRole.userId to userRep.id,
                PutUserRole.roleName to JwtRole.SUPERUSER
            )
        ) {}

        piperTest.test(
            endpointConfig = GetUser.endpointConfig,
            pathParams = mapOf(GetUser.userId to userRep.id)
        ) {
            val actual = json.parse<UserRep.Complete>(response.content!!)
            assertEquals(userRep, actual)
        }
    }

    @Test
    fun happyPathIdempotent() {

        val orgId = UUID.randomUUID()

        var userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = PostUser.endpointConfig,
            body = UserRepFixtures.jeffHudsonFixture.creation(orgId)
        )

        userRep = userRep.copy(roles = userRep.roles.plus(JwtRole.SUPERUSER))
        piperTest.setup(
            endpointConfig = PutUserRole.endpointConfig,
            pathParams = mapOf(
                PutUserRole.userId to userRep.id,
                PutUserRole.roleName to JwtRole.SUPERUSER
            )
        )

        piperTest.test(
            endpointConfig = PutUserRole.endpointConfig,
            pathParams = mapOf(
                PutUserRole.userId to userRep.id,
                PutUserRole.roleName to JwtRole.SUPERUSER
            )
        ) {}

        piperTest.test(
            endpointConfig = GetUser.endpointConfig,
            pathParams = mapOf(GetUser.userId to userRep.id)
        ) {
            val actual = json.parse<UserRep.Complete>(response.content!!)
            assertEquals(userRep, actual)
        }
    }
}

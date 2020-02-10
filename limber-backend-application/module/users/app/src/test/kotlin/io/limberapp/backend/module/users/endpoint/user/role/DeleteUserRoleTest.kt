package io.limberapp.backend.module.users.endpoint.user.role

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.endpoint.user.PostUser
import io.limberapp.backend.module.users.endpoint.user.GetUser
import io.limberapp.backend.module.users.exception.account.UserDoesNotHaveRole
import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.user.UserRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class DeleteUserRoleTest : ResourceTest() {

    @Test
    fun userDoesNotExist() {

        // Setup
        val userId = UUID.randomUUID()

        // DeleteUserRole
        piperTest.test(
            endpointConfig = DeleteUserRole.endpointConfig,
            pathParams = mapOf(
                DeleteUserRole.userId to userId,
                DeleteUserRole.roleName to JwtRole.SUPERUSER
            ),
            expectedException = UserNotFound()
        )
    }

    @Test
    fun roleDoesNotExist() {

        // PostUser
        val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, 0)
        piperTest.setup(
            endpointConfig = PostUser.endpointConfig,
            body = UserRepFixtures.jeffHudsonFixture.creation()
        )

        // DeleteUserRole
        piperTest.test(
            endpointConfig = DeleteUserRole.endpointConfig,
            pathParams = mapOf(
                DeleteUserRole.userId to userRep.id,
                DeleteUserRole.roleName to JwtRole.SUPERUSER
            ),
            expectedException = UserDoesNotHaveRole(JwtRole.SUPERUSER)
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

    @Test
    fun happyPath() {

        // PostUser
        var userRep = UserRepFixtures.jeffHudsonFixture.complete(this, 0)
        piperTest.setup(
            endpointConfig = PostUser.endpointConfig,
            body = UserRepFixtures.jeffHudsonFixture.creation()
        )

        // PutUserRole
        userRep = userRep.copy(roles = userRep.roles.plus(JwtRole.SUPERUSER))
        piperTest.setup(
            endpointConfig = PutUserRole.endpointConfig,
            pathParams = mapOf(
                PutUserRole.userId to userRep.id,
                PutUserRole.roleName to JwtRole.SUPERUSER
            )
        )

        // DeleteUserRole
        userRep = userRep.copy(roles = userRep.roles.filter { it != JwtRole.SUPERUSER }.toSet())
        piperTest.test(
            endpointConfig = DeleteUserRole.endpointConfig,
            pathParams = mapOf(
                DeleteUserRole.userId to userRep.id,
                DeleteUserRole.roleName to JwtRole.SUPERUSER
            )
        ) {}

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

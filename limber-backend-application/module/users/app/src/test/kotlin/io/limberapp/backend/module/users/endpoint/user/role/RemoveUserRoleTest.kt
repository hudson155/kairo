package io.limberapp.backend.module.users.endpoint.user.role

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.endpoint.user.CreateUser
import io.limberapp.backend.module.users.endpoint.user.GetUser
import io.limberapp.backend.module.users.exception.notFound.UserNotFound
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.user.UserRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class RemoveUserRoleTest : ResourceTest() {

    @Test
    fun userDoesNotExist() {

        // Setup
        val userId = UUID.randomUUID()

        // RemoveUserRole
        piperTest.test(
            endpointConfig = RemoveUserRole.endpointConfig,
            pathParams = mapOf(
                RemoveUserRole.userId to userId,
                RemoveUserRole.roleName to JwtRole.SUPERUSER
            ),
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

        // AddUserRole
        userRep = userRep.copy(roles = userRep.roles.plus(JwtRole.SUPERUSER))
        piperTest.setup(
            endpointConfig = AddUserRole.endpointConfig,
            pathParams = mapOf(
                AddUserRole.userId to userRep.id,
                AddUserRole.roleName to JwtRole.SUPERUSER
            )
        )

        // RemoveUserRole
        userRep = userRep.copy(roles = userRep.roles.filter { it != JwtRole.SUPERUSER }.toSet())
        piperTest.test(
            endpointConfig = RemoveUserRole.endpointConfig,
            pathParams = mapOf(
                RemoveUserRole.userId to userRep.id,
                RemoveUserRole.roleName to JwtRole.SUPERUSER
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

    @Test
    fun happyPathIdempotent() {

        // CreateUser
        val userRep = UserRepFixtures[0].complete(this, 0)
        piperTest.setup(
            endpointConfig = CreateUser.endpointConfig,
            body = UserRepFixtures[0].creation()
        )

        // RemoveUserRole
        piperTest.test(
            endpointConfig = RemoveUserRole.endpointConfig,
            pathParams = mapOf(
                RemoveUserRole.userId to userRep.id,
                RemoveUserRole.roleName to JwtRole.SUPERUSER
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

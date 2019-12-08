package io.limberapp.backend.module.users.endpoint.user.role

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.endpoint.user.CreateUser
import io.limberapp.backend.module.users.endpoint.user.GetUser
import io.limberapp.backend.module.users.exception.notFound.UserNotFound
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.user.UserRepFixtures
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class AddUserRoleTest : ResourceTest() {

    @Test
    fun userDoesNotExist() {

        // Setup
        val userId = UUID.randomUUID()

        // AddUserRole
        piperTest.test(
            endpointConfig = AddUserRole.endpointConfig,
            pathParams = mapOf(
                AddUserRole.userId to userId.toString(),
                AddUserRole.roleName to JwtRole.SUPERUSER.toString()
            ),
            expectedException = UserNotFound()
        )
    }

    @Test
    fun happyPath() {

        // CreateUser
        var userRep = UserRepFixtures[0].complete(this, 0)
        piperTest.test(
            endpointConfig = CreateUser.endpointConfig,
            body = UserRepFixtures[0].creation()
        ) {}

        // AddUserRole
        userRep = userRep.copy(roles = userRep.roles.plus(JwtRole.SUPERUSER))
        piperTest.test(
            endpointConfig = AddUserRole.endpointConfig,
            pathParams = mapOf(
                AddUserRole.userId to userRep.id.toString(),
                AddUserRole.roleName to JwtRole.SUPERUSER.toString()
            )
        ) {}

        // GetUser
        piperTest.test(
            endpointConfig = GetUser.endpointConfig,
            pathParams = mapOf(GetUser.userId to userRep.id.toString())
        ) {
            val actual = objectMapper.readValue<UserRep.Complete>(response.content!!)
            assertEquals(userRep, actual)
        }
    }

    @Test
    fun happyPathIdempotent() {

        // CreateUser
        var userRep = UserRepFixtures[0].complete(this, 0)
        piperTest.test(
            endpointConfig = CreateUser.endpointConfig,
            body = UserRepFixtures[0].creation()
        ) {}

        // AddUserRole
        userRep = userRep.copy(roles = userRep.roles.plus(JwtRole.SUPERUSER))
        piperTest.test(
            endpointConfig = AddUserRole.endpointConfig,
            pathParams = mapOf(
                AddUserRole.userId to userRep.id.toString(),
                AddUserRole.roleName to JwtRole.SUPERUSER.toString()
            )
        ) {}

        // AddUserRole
        piperTest.test(
            endpointConfig = AddUserRole.endpointConfig,
            pathParams = mapOf(
                AddUserRole.userId to userRep.id.toString(),
                AddUserRole.roleName to JwtRole.SUPERUSER.toString()
            )
        ) {}

        // GetUser
        piperTest.test(
            endpointConfig = GetUser.endpointConfig,
            pathParams = mapOf(GetUser.userId to userRep.id.toString())
        ) {
            val actual = objectMapper.readValue<UserRep.Complete>(response.content!!)
            assertEquals(userRep, actual)
        }
    }
}

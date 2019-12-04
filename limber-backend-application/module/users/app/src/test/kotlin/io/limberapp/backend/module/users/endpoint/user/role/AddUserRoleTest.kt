package io.limberapp.backend.module.users.endpoint.user.role

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.endpoint.user.CreateUser
import io.limberapp.backend.module.users.endpoint.user.GetUser
import io.limberapp.backend.module.users.exception.notFound.UserNotFound
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import org.junit.Test
import java.time.LocalDateTime
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
    fun happyPathIdempotent() {

        // CreateUser
        val userCreationRep = UserRep.Creation(
            firstName = "Jeff",
            lastName = "Hudson",
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null
        )
        var userRep = UserRep.Complete(
            id = deterministicUuidGenerator[0],
            created = LocalDateTime.now(fixedClock),
            firstName = userCreationRep.firstName,
            lastName = userCreationRep.lastName,
            emailAddress = userCreationRep.emailAddress,
            profilePhotoUrl = userCreationRep.profilePhotoUrl,
            roles = emptySet()
        )
        piperTest.test(
            endpointConfig = CreateUser.endpointConfig,
            body = userCreationRep
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

    @Test
    fun happyPath() {

        // CreateUser
        val userCreationRep = UserRep.Creation(
            firstName = "Jeff",
            lastName = "Hudson",
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null
        )
        var userRep = UserRep.Complete(
            id = deterministicUuidGenerator[0],
            created = LocalDateTime.now(fixedClock),
            firstName = userCreationRep.firstName,
            lastName = userCreationRep.lastName,
            emailAddress = userCreationRep.emailAddress,
            profilePhotoUrl = userCreationRep.profilePhotoUrl,
            roles = emptySet()
        )
        piperTest.test(
            endpointConfig = CreateUser.endpointConfig,
            body = userCreationRep
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
}

package io.limberapp.backend.module.users.endpoint.user.role

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.HttpStatusCode
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.endpoint.user.CreateUser
import io.limberapp.backend.module.users.endpoint.user.GetUser
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

internal class AddUserRoleTest : ResourceTest() {

    @Test
    fun userDoesNotExist() {
        piperTest.test(
            endpointConfig = AddUserRole.endpointConfig,
            pathParams = mapOf(
                AddUserRole.userId to UUID.randomUUID().toString(),
                AddUserRole.roleName to JwtRole.SUPERUSER.toString()
            ),
            expectedStatusCode = HttpStatusCode.NotFound
        ) {}
    }

    @Test
    fun userExists() {

        val creationRep = UserRep.Creation(
            firstName = "Jeff",
            lastName = "Hudson",
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null
        )
        val id = deterministicUuidGenerator[0]
        piperTest.test(
            endpointConfig = CreateUser.endpointConfig,
            body = creationRep
        ) {}

        piperTest.test(
            endpointConfig = AddUserRole.endpointConfig,
            pathParams = mapOf(
                AddUserRole.userId to id.toString(),
                AddUserRole.roleName to JwtRole.SUPERUSER.toString()
            )
        ) {}

        piperTest.test(
            endpointConfig = GetUser.endpointConfig,
            pathParams = mapOf(GetUser.userId to id.toString())
        ) {
            val actual = objectMapper.readValue<UserRep.Complete>(response.content!!)
            val expected = UserRep.Complete(
                id = id,
                created = LocalDateTime.now(fixedClock),
                firstName = creationRep.firstName,
                lastName = creationRep.lastName,
                emailAddress = creationRep.emailAddress,
                profilePhotoUrl = creationRep.profilePhotoUrl,
                roles = setOf(JwtRole.SUPERUSER)
            )
            assertEquals(expected, actual)
        }
    }

    @Test
    fun userIsAlreadySuperuser() {

        val creationRep = UserRep.Creation(
            firstName = "Jeff",
            lastName = "Hudson",
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null
        )
        val id = deterministicUuidGenerator[0]
        piperTest.test(
            endpointConfig = CreateUser.endpointConfig,
            body = creationRep
        ) {}

        piperTest.test(
            endpointConfig = AddUserRole.endpointConfig,
            pathParams = mapOf(
                AddUserRole.userId to id.toString(),
                AddUserRole.roleName to JwtRole.SUPERUSER.toString()
            )
        ) {}

        piperTest.test(
            endpointConfig = AddUserRole.endpointConfig,
            pathParams = mapOf(
                AddUserRole.userId to id.toString(),
                AddUserRole.roleName to JwtRole.SUPERUSER.toString()
            ),
            expectedStatusCode = HttpStatusCode.Conflict
        ) {}
    }
}

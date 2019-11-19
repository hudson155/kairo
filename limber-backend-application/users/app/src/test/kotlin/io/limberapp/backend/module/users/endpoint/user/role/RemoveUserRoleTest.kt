package io.limberapp.backend.module.users.endpoint.user.role

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.HttpStatusCode
import io.limberapp.backend.module.users.endpoint.user.CreateUser
import io.limberapp.backend.module.users.endpoint.user.GetUser
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.framework.endpoint.authorization.jwt.JwtRole
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

internal class RemoveUserRoleTest : ResourceTest() {

    @Test
    fun userDoesNotExist() {
        limberTest.test(
            endpointConfig = RemoveUserRole.endpointConfig,
            pathParams = mapOf(
                RemoveUserRole.userId to UUID.randomUUID().toString(),
                RemoveUserRole.roleName to JwtRole.SUPERUSER.toString()
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

        limberTest.test(
            endpointConfig = CreateUser.endpointConfig,
            body = creationRep
        ) {
            val actual = objectMapper.readValue<UserRep.Complete>(response.content!!)
            val expected = UserRep.Complete(
                id = id,
                created = LocalDateTime.now(fixedClock),
                firstName = creationRep.firstName,
                lastName = creationRep.lastName,
                emailAddress = creationRep.emailAddress,
                profilePhotoUrl = creationRep.profilePhotoUrl,
                roles = emptySet()
            )
            assertEquals(expected, actual)
        }

        limberTest.test(
            endpointConfig = AddUserRole.endpointConfig,
            pathParams = mapOf(
                AddUserRole.userId to id.toString(),
                AddUserRole.roleName to JwtRole.SUPERUSER.toString()
            )
        ) {}

        limberTest.test(
            endpointConfig = RemoveUserRole.endpointConfig,
            pathParams = mapOf(
                RemoveUserRole.userId to id.toString(),
                RemoveUserRole.roleName to JwtRole.SUPERUSER.toString()
            )
        ) {}

        limberTest.test(
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
                roles = setOf()
            )
            assertEquals(expected, actual)
        }
    }

    @Test
    fun userIsNotSuperuser() {

        val creationRep = UserRep.Creation(
            firstName = "Jeff",
            lastName = "Hudson",
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null
        )
        val id = deterministicUuidGenerator[0]

        limberTest.test(
            endpointConfig = CreateUser.endpointConfig,
            body = creationRep
        ) {
            val actual = objectMapper.readValue<UserRep.Complete>(response.content!!)
            val expected = UserRep.Complete(
                id = id,
                created = LocalDateTime.now(fixedClock),
                firstName = creationRep.firstName,
                lastName = creationRep.lastName,
                emailAddress = creationRep.emailAddress,
                profilePhotoUrl = creationRep.profilePhotoUrl,
                roles = emptySet()
            )
            assertEquals(expected, actual)
        }

        limberTest.test(
            endpointConfig = RemoveUserRole.endpointConfig,
            pathParams = mapOf(
                RemoveUserRole.userId to id.toString(),
                RemoveUserRole.roleName to JwtRole.SUPERUSER.toString()
            ),
            expectedStatusCode = HttpStatusCode.Conflict
        ) {}
    }
}

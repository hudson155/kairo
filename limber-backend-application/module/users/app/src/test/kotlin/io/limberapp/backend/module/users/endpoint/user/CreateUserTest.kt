package io.limberapp.backend.module.users.endpoint.user

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.HttpStatusCode
import io.limberapp.backend.module.users.exception.conflict.ConflictsWithAnotherUser
import io.limberapp.backend.module.users.exception.notFound.UserNotFound
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

internal class CreateUserTest : ResourceTest() {

    @Test
    fun duplicateEmailAddress() {

        // CreateUser
        val user1CreationRep = UserRep.Creation(
            firstName = "Jeff",
            lastName = "Hudson",
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null
        )
        piperTest.test(
            endpointConfig = CreateUser.endpointConfig,
            body = user1CreationRep
        ) {}

        // CreateUser
        val user2CreationRep = UserRep.Creation(
            firstName = "Summer",
            lastName = "Kavan",
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null
        )
        piperTest.test(
            endpointConfig = CreateUser.endpointConfig,
            body = user2CreationRep,
            expectedException = ConflictsWithAnotherUser()
        )
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
        val userRep = UserRep.Complete(
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
        ) {
            val actual = objectMapper.readValue<UserRep.Complete>(response.content!!)
            assertEquals(userRep, actual)
        }

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

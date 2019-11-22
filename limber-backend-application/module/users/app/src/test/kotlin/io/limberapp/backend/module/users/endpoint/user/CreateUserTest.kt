package io.limberapp.backend.module.users.endpoint.user

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.HttpStatusCode
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

internal class CreateUserTest : ResourceTest() {

    @Test
    fun create() {
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
    }

    @Test
    fun duplicateEmailAddress() {

        val creationRep1 = UserRep.Creation(
            firstName = "Jeff",
            lastName = "Hudson",
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null
        )
        limberTest.test(
            endpointConfig = CreateUser.endpointConfig,
            body = creationRep1
        ) {}

        val creationRep2 = UserRep.Creation(
            firstName = "Jeff",
            lastName = "Hudson",
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null
        )
        limberTest.test(
            endpointConfig = CreateUser.endpointConfig,
            body = creationRep2,
            expectedStatusCode = HttpStatusCode.Conflict
        ) {}
    }
}

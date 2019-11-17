package io.limberapp.backend.module.users.endpoint.user

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.HttpStatusCode
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

internal class GetUserByEmailAddressTest : ResourceTest() {

    @Test
    fun doesNotExist() {
        val emailAddress = "jhudson@jhudson.ca"
        limberTest.test(
            endpointConfig = GetUserByEmailAddress.endpointConfig,
            queryParams = mapOf(GetUserByEmailAddress.emailAddress to emailAddress),
            expectedStatusCode = HttpStatusCode.NotFound
        ) {}
    }

    @Test
    fun exists() {

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
        ) {}

        limberTest.test(
            endpointConfig = GetUserByEmailAddress.endpointConfig,
            queryParams = mapOf(GetUserByEmailAddress.emailAddress to creationRep.emailAddress)
        ) {
            val actual = objectMapper.readValue<UserRep.Complete>(response.content!!)
            val expected = UserRep.Complete(
                id = id,
                created = LocalDateTime.now(fixedClock),
                firstName = creationRep.firstName,
                lastName = creationRep.lastName,
                emailAddress = creationRep.emailAddress,
                profilePhotoUrl = creationRep.profilePhotoUrl
            )
            assertEquals(expected, actual)
        }
    }
}

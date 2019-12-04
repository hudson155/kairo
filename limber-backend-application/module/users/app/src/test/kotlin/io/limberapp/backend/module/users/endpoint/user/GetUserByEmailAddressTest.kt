package io.limberapp.backend.module.users.endpoint.user

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.HttpStatusCode
import io.limberapp.backend.module.users.exception.notFound.UserNotFound
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

internal class GetUserByEmailAddressTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        // Setup
        val emailAddress = "jhudson@jhudson.ca"

        // GetUserByEmailAddress
        piperTest.test(
            endpointConfig = GetUserByEmailAddress.endpointConfig,
            queryParams = mapOf(GetUserByEmailAddress.emailAddress to emailAddress),
            expectedException = UserNotFound()
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
        ) {}

        piperTest.test(
            endpointConfig = GetUserByEmailAddress.endpointConfig,
            queryParams = mapOf(GetUserByEmailAddress.emailAddress to userCreationRep.emailAddress)
        ) {
            val actual = objectMapper.readValue<UserRep.Complete>(response.content!!)
            assertEquals(userRep, actual)
        }
    }
}

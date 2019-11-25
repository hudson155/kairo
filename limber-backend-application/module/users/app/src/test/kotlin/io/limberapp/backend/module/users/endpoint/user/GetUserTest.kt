package io.limberapp.backend.module.users.endpoint.user

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.HttpStatusCode
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

internal class GetUserTest : ResourceTest() {

    @Test
    fun doesNotExist() {
        val userId = UUID.randomUUID()
        piperTest.test(
            endpointConfig = GetUser.endpointConfig,
            pathParams = mapOf(GetUser.userId to userId.toString()),
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
        piperTest.test(
            endpointConfig = CreateUser.endpointConfig,
            body = creationRep
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
                roles = emptySet()
            )
            assertEquals(expected, actual)
        }
    }
}

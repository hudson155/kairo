package io.limberapp.backend.module.users.endpoint.user

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.users.exception.notFound.UserNotFound
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

internal class GetUserTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        // Setup
        val userId = UUID.randomUUID()

        // GetUser
        piperTest.test(
            endpointConfig = GetUser.endpointConfig,
            pathParams = mapOf(GetUser.userId to userId.toString()),
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
            endpointConfig = GetUser.endpointConfig,
            pathParams = mapOf(GetUser.userId to userRep.id.toString())
        ) {
            val actual = objectMapper.readValue<UserRep.Complete>(response.content!!)
            assertEquals(userRep, actual)
        }
    }
}

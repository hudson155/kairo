package io.limberapp.backend.module.users.endpoint.user

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.HttpStatusCode
import io.limberapp.backend.module.users.exception.notFound.UserNotFound
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

internal class UpdateUserTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        // Setup
        val userId = UUID.randomUUID()

        // UpdateUser
        val updateRep = UserRep.Update(firstName = "Gunner")
        piperTest.test(
            endpointConfig = UpdateUser.endpointConfig,
            pathParams = mapOf(UpdateUser.userId to userId.toString()),
            body = updateRep,
            expectedException = UserNotFound()
        )
    }

    @Test
    fun happyPath() {

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

        // UpdateUser
        val updateRep = UserRep.Update(firstName = "Gunner")
        userRep = userRep.copy(firstName = updateRep.firstName!!)
        piperTest.test(
            endpointConfig = UpdateUser.endpointConfig,
            pathParams = mapOf(UpdateUser.userId to userRep.id.toString()),
            body = updateRep
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

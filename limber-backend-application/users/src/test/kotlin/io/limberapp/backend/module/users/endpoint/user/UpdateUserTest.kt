package io.limberapp.backend.module.users.endpoint.user

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.HttpStatusCode
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

internal class UpdateUserTest : ResourceTest() {

    @Test
    fun doesNotExist() {
        val userId = UUID.randomUUID()
        val updateRep = UserRep.Update(firstName = "Gunner")
        limberTest.test(
            config = UpdateUser.config,
            pathParams = mapOf("userId" to userId.toString()),
            body = updateRep,
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
        val id = uuidGenerator[0]
        limberTest.test(
            config = CreateUser.config,
            body = creationRep
        ) {}

        val updateRep = UserRep.Update(firstName = "Gunner")
        limberTest.test(
            config = UpdateUser.config,
            pathParams = mapOf("userId" to id.toString()),
            body = updateRep
        ) {
            val actual = objectMapper.readValue<UserRep.Complete>(response.content!!)
            val expected = UserRep.Complete(
                id = id,
                created = LocalDateTime.now(clock),
                firstName = updateRep.firstName!!,
                lastName = creationRep.lastName,
                emailAddress = creationRep.emailAddress,
                profilePhotoUrl = creationRep.profilePhotoUrl
            )
            assertEquals(expected, actual)
        }
    }
}

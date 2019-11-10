package io.limberapp.backend.module.users.endpoint.user

import io.ktor.http.HttpStatusCode
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import org.junit.Test
import java.util.UUID

internal class DeleteUserTest : ResourceTest() {

    @Test
    fun doesNotExist() {
        val userId = UUID.randomUUID()
        limberTest.test(
            endpointConfig = DeleteUser.endpointConfig,
            pathParams = mapOf(DeleteUser.userId to userId.toString()),
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
            endpointConfig = CreateUser.endpointConfig,
            body = creationRep
        ) {}

        limberTest.test(
            endpointConfig = DeleteUser.endpointConfig,
            pathParams = mapOf(DeleteUser.userId to id.toString())
        ) {}

        limberTest.test(
            endpointConfig = GetUser.endpointConfig,
            pathParams = mapOf(GetUser.userId to id.toString()),
            expectedStatusCode = HttpStatusCode.NotFound
        ) {}
    }
}

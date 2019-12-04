package io.limberapp.backend.module.users.endpoint.user

import io.ktor.http.HttpStatusCode
import io.limberapp.backend.module.users.exception.notFound.UserNotFound
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import org.junit.Test
import java.util.UUID

internal class DeleteUserTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        // Setup
        val userId = UUID.randomUUID()

        // DeleteUser
        piperTest.test(
            endpointConfig = DeleteUser.endpointConfig,
            pathParams = mapOf(DeleteUser.userId to userId.toString()),
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
        val userId = deterministicUuidGenerator[0]
        piperTest.test(
            endpointConfig = CreateUser.endpointConfig,
            body = userCreationRep
        ) {}

        // DeleteUser
        piperTest.test(
            endpointConfig = DeleteUser.endpointConfig,
            pathParams = mapOf(DeleteUser.userId to userId.toString())
        ) {}

        // GetUser
        piperTest.test(
            endpointConfig = GetUser.endpointConfig,
            pathParams = mapOf(GetUser.userId to userId.toString()),
            expectedException = UserNotFound()
        )
    }
}

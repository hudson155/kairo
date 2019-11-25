package io.limberapp.backend.module.auth.endpoint.personalAccessToken

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.HttpStatusCode
import io.limberapp.backend.module.auth.rep.personalAccessToken.PersonalAccessTokenRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import org.junit.Test
import java.util.UUID
import kotlin.test.assertTrue

internal class DeletePersonalAccessTokenTest : ResourceTest() {

    @Test
    fun doesNotExist() {
        piperTest.test(
            endpointConfig = DeletePersonalAccessToken.endpointConfig,
            pathParams = mapOf(
                DeletePersonalAccessToken.userId to UUID.randomUUID().toString(),
                DeletePersonalAccessToken.personalAccessTokenId to UUID.randomUUID().toString()
            ),
            expectedStatusCode = HttpStatusCode.NotFound
        ) {}
    }

    @Test
    fun exists() {

        val userId = UUID.randomUUID()
        val id = deterministicUuidGenerator[0]
        piperTest.test(
            endpointConfig = CreatePersonalAccessToken.endpointConfig,
            pathParams = mapOf(CreatePersonalAccessToken.userId to userId.toString())
        ) {}

        piperTest.test(
            endpointConfig = DeletePersonalAccessToken.endpointConfig,
            pathParams = mapOf(
                DeletePersonalAccessToken.userId to userId.toString(),
                DeletePersonalAccessToken.personalAccessTokenId to id.toString()
            )
        ) {}

        piperTest.test(
            endpointConfig = GetPersonalAccessTokensByUserId.endpointConfig,
            pathParams = mapOf(CreatePersonalAccessToken.userId to userId.toString())
        ) {
            val actual = objectMapper.readValue<List<PersonalAccessTokenRep.Complete>>(response.content!!)
            assertTrue(actual.isEmpty())
        }
    }
}

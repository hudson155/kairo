package io.limberapp.backend.module.auth.endpoint.personalAccessToken

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.auth.exception.notFound.PersonalAccessTokenNotFound
import io.limberapp.backend.module.auth.rep.personalAccessToken.PersonalAccessTokenRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

internal class DeletePersonalAccessTokenTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        // Setup
        val userId = UUID.randomUUID()
        val personalAccessTokenId = UUID.randomUUID()

        // DeletePersonalAccessToken
        piperTest.test(
            endpointConfig = DeletePersonalAccessToken.endpointConfig,
            pathParams = mapOf(
                DeletePersonalAccessToken.userId to userId.toString(),
                DeletePersonalAccessToken.personalAccessTokenId to personalAccessTokenId.toString()
            ),
            expectedException = PersonalAccessTokenNotFound()
        )
    }

    @Test
    fun happyPath() {

        // Setup
        val userId = UUID.randomUUID()

        // CreatePersonalAccessToken
        val personalAccessToken0Id = deterministicUuidGenerator[0]
        piperTest.test(
            endpointConfig = CreatePersonalAccessToken.endpointConfig,
            pathParams = mapOf(CreatePersonalAccessToken.userId to userId.toString())
        ) {}

        // CreatePersonalAccessToken
        val personalAccessToken1Rep = PersonalAccessTokenRep.Complete(
            id = deterministicUuidGenerator[2],
            created = LocalDateTime.now(fixedClock),
            userId = userId
        )
        piperTest.test(
            endpointConfig = CreatePersonalAccessToken.endpointConfig,
            pathParams = mapOf(CreatePersonalAccessToken.userId to userId.toString())
        ) {}

        // DeletePersonalAccessToken
        piperTest.test(
            endpointConfig = DeletePersonalAccessToken.endpointConfig,
            pathParams = mapOf(
                DeletePersonalAccessToken.userId to userId.toString(),
                DeletePersonalAccessToken.personalAccessTokenId to personalAccessToken0Id.toString()
            )
        ) {}

        // GetPersonalAccessTokensByUserId
        piperTest.test(
            endpointConfig = GetPersonalAccessTokensByUserId.endpointConfig,
            pathParams = mapOf(CreatePersonalAccessToken.userId to userId.toString())
        ) {
            val actual = objectMapper.readValue<List<PersonalAccessTokenRep.Complete>>(response.content!!)
            val expected = listOf(personalAccessToken1Rep)
            assertEquals(expected, actual)
        }
    }
}

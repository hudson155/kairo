package io.limberapp.backend.module.auth.endpoint.personalAccessToken

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.auth.exception.notFound.PersonalAccessTokenNotFound
import io.limberapp.backend.module.auth.rep.personalAccessToken.PersonalAccessTokenRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.personalAccessToken.PersonalAccessTokenRepFixtures
import org.junit.Test
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
        val personalAccessToken0Rep = PersonalAccessTokenRepFixtures[0].complete(this, userId, 0)
        piperTest.setup(
            endpointConfig = CreatePersonalAccessToken.endpointConfig,
            pathParams = mapOf(CreatePersonalAccessToken.userId to userId.toString())
        )

        // CreatePersonalAccessToken
        val personalAccessToken1Rep = PersonalAccessTokenRepFixtures[0].complete(this, userId, 2)
        piperTest.setup(
            endpointConfig = CreatePersonalAccessToken.endpointConfig,
            pathParams = mapOf(CreatePersonalAccessToken.userId to userId.toString())
        )

        // DeletePersonalAccessToken
        piperTest.test(
            endpointConfig = DeletePersonalAccessToken.endpointConfig,
            pathParams = mapOf(
                DeletePersonalAccessToken.userId to userId.toString(),
                DeletePersonalAccessToken.personalAccessTokenId to personalAccessToken0Rep.id.toString()
            )
        ) {}

        // GetPersonalAccessTokensByUserId
        piperTest.test(
            endpointConfig = GetPersonalAccessTokensByUserId.endpointConfig,
            pathParams = mapOf(CreatePersonalAccessToken.userId to userId.toString())
        ) {
            val actual = objectMapper.readValue<List<PersonalAccessTokenRep.Complete>>(response.content!!)
            assertEquals(listOf(personalAccessToken1Rep), actual)
        }
    }
}

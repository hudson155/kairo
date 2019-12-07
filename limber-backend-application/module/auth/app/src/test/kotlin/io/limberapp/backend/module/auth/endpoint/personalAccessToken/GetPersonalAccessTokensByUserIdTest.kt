package io.limberapp.backend.module.auth.endpoint.personalAccessToken

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.auth.rep.personalAccessToken.PersonalAccessTokenRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.personalAccessToken.PersonalAccessTokenRepFixtures
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetPersonalAccessTokensByUserIdTest : ResourceTest() {

    @Test
    fun happyPathNoneExist() {

        // Setup
        val userId = UUID.randomUUID()

        // GetPersonalAccessTokensByUserId
        piperTest.test(
            endpointConfig = GetPersonalAccessTokensByUserId.endpointConfig,
            pathParams = mapOf(CreatePersonalAccessToken.userId to userId.toString())
        ) {
            val actual = objectMapper.readValue<List<PersonalAccessTokenRep.Complete>>(response.content!!)
            assertTrue(actual.isEmpty())
        }
    }

    @Test
    fun happyPathSomeExist() {

        // Setup
        val userId = UUID.randomUUID()

        // CreatePersonalAccessToken
        val personalAccessToken0Rep = PersonalAccessTokenRepFixtures.Complete[0](userId, 0)
        piperTest.test(
            endpointConfig = CreatePersonalAccessToken.endpointConfig,
            pathParams = mapOf(CreatePersonalAccessToken.userId to userId.toString())
        ) {}

        // CreatePersonalAccessToken
        val personalAccessToken1Rep = PersonalAccessTokenRepFixtures.Complete[0](userId, 2)
        piperTest.test(
            endpointConfig = CreatePersonalAccessToken.endpointConfig,
            pathParams = mapOf(CreatePersonalAccessToken.userId to userId.toString())
        ) {}

        // GetPersonalAccessTokensByUserId
        piperTest.test(
            endpointConfig = GetPersonalAccessTokensByUserId.endpointConfig,
            pathParams = mapOf(CreatePersonalAccessToken.userId to userId.toString())
        ) {
            val actual = objectMapper.readValue<List<PersonalAccessTokenRep.Complete>>(response.content!!)
            assertEquals(listOf(personalAccessToken0Rep, personalAccessToken1Rep), actual)
        }
    }
}

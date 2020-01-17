package io.limberapp.backend.module.auth.endpoint.accessToken

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.auth.rep.accessToken.AccessTokenRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.accessToken.AccessTokenRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetAccessTokensByUserIdTest : ResourceTest() {

    @Test
    fun happyPathNoneExist() {

        // Setup
        val userId = UUID.randomUUID()

        // GetAccessTokensByUserId
        piperTest.test(
            endpointConfig = GetAccessTokensByUserId.endpointConfig,
            pathParams = mapOf(CreateAccessToken.userId to userId)
        ) {
            val actual = objectMapper.readValue<List<AccessTokenRep.Complete>>(response.content!!)
            assertTrue(actual.isEmpty())
        }
    }

    @Test
    fun happyPathSomeExist() {

        // Setup
        val userId = UUID.randomUUID()

        // CreateAccessToken
        val accessToken0Rep = AccessTokenRepFixtures[0].complete(this, userId, 0)
        piperTest.setup(
            endpointConfig = CreateAccessToken.endpointConfig,
            pathParams = mapOf(CreateAccessToken.userId to userId)
        ) {}

        // CreateAccessToken
        val accessToken1Rep = AccessTokenRepFixtures[0].complete(this, userId, 2)
        piperTest.setup(
            endpointConfig = CreateAccessToken.endpointConfig,
            pathParams = mapOf(CreateAccessToken.userId to userId)
        ) {}

        // GetAccessTokensByUserId
        piperTest.test(
            endpointConfig = GetAccessTokensByUserId.endpointConfig,
            pathParams = mapOf(CreateAccessToken.userId to userId)
        ) {
            val actual = objectMapper.readValue<List<AccessTokenRep.Complete>>(response.content!!)
            assertEquals(listOf(accessToken0Rep, accessToken1Rep), actual)
        }
    }
}

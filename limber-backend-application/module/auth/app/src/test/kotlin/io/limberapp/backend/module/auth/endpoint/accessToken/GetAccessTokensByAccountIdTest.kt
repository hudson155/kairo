package io.limberapp.backend.module.auth.endpoint.accessToken

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.auth.rep.accessToken.AccessTokenRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.accessToken.AccessTokenRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetAccessTokensByAccountIdTest : ResourceTest() {

    @Test
    fun happyPathNoneExist() {

        // Setup
        val accountId = UUID.randomUUID()

        // GetAccessTokensByAccountId
        piperTest.test(
            endpointConfig = GetAccessTokensByAccountId.endpointConfig,
            pathParams = mapOf(CreateAccessToken.accountId to accountId)
        ) {
            val actual = objectMapper.readValue<List<AccessTokenRep.Complete>>(response.content!!)
            assertTrue(actual.isEmpty())
        }
    }

    @Test
    fun happyPathSomeExist() {

        // Setup
        val accountId = UUID.randomUUID()

        // CreateAccessToken
        val accessToken0Rep = AccessTokenRepFixtures[0].complete(this, accountId, 0)
        piperTest.setup(
            endpointConfig = CreateAccessToken.endpointConfig,
            pathParams = mapOf(CreateAccessToken.accountId to accountId)
        ) {}

        // CreateAccessToken
        val accessToken1Rep = AccessTokenRepFixtures[0].complete(this, accountId, 2)
        piperTest.setup(
            endpointConfig = CreateAccessToken.endpointConfig,
            pathParams = mapOf(CreateAccessToken.accountId to accountId)
        ) {}

        // GetAccessTokensByAccountId
        piperTest.test(
            endpointConfig = GetAccessTokensByAccountId.endpointConfig,
            pathParams = mapOf(CreateAccessToken.accountId to accountId)
        ) {
            val actual = objectMapper.readValue<List<AccessTokenRep.Complete>>(response.content!!)
            assertEquals(listOf(accessToken0Rep, accessToken1Rep), actual)
        }
    }
}

package io.limberapp.backend.module.auth.endpoint.accessToken

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.auth.exception.accessToken.AccessTokenNotFound
import io.limberapp.backend.module.auth.rep.accessToken.AccessTokenRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.accessToken.AccessTokenRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class DeleteAccessTokenTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        // Setup
        val accountId = UUID.randomUUID()
        val accessTokenId = UUID.randomUUID()

        // DeleteAccessToken
        piperTest.test(
            endpointConfig = DeleteAccessToken.endpointConfig,
            pathParams = mapOf(
                DeleteAccessToken.accountId to accountId,
                DeleteAccessToken.accessTokenId to accessTokenId
            ),
            expectedException = AccessTokenNotFound()
        )
    }

    @Test
    fun happyPath() {

        // Setup
        val accountId = UUID.randomUUID()

        // PostAccessToken
        val accessToken0Rep = AccessTokenRepFixtures.fixture.complete(this, accountId, 0)
        piperTest.setup(
            endpointConfig = PostAccessToken.endpointConfig,
            pathParams = mapOf(PostAccessToken.accountId to accountId)
        )

        // PostAccessToken
        val accessToken1Rep = AccessTokenRepFixtures.fixture.complete(this, accountId, 2)
        piperTest.setup(
            endpointConfig = PostAccessToken.endpointConfig,
            pathParams = mapOf(PostAccessToken.accountId to accountId)
        )

        // DeleteAccessToken
        piperTest.test(
            endpointConfig = DeleteAccessToken.endpointConfig,
            pathParams = mapOf(
                DeleteAccessToken.accountId to accountId,
                DeleteAccessToken.accessTokenId to accessToken0Rep.id
            )
        ) {}

        // GetAccessTokensByAccountId
        piperTest.test(
            endpointConfig = GetAccessTokensByAccountId.endpointConfig,
            pathParams = mapOf(PostAccessToken.accountId to accountId)
        ) {
            val actual = objectMapper.readValue<List<AccessTokenRep.Complete>>(response.content!!)
            assertEquals(listOf(accessToken1Rep), actual)
        }
    }
}

package io.limberapp.backend.module.auth.endpoint.accessToken

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.auth.exception.notFound.AccessTokenNotFound
import io.limberapp.backend.module.auth.rep.accessToken.AccessTokenRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.accessToken.AccessTokenRepFixtures
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class DeleteAccessTokenTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        // Setup
        val userId = UUID.randomUUID()
        val accessTokenId = UUID.randomUUID()

        // DeleteAccessToken
        piperTest.test(
            endpointConfig = DeleteAccessToken.endpointConfig,
            pathParams = mapOf(
                DeleteAccessToken.userId to userId,
                DeleteAccessToken.accessTokenId to accessTokenId
            ),
            expectedException = AccessTokenNotFound()
        )
    }

    @Test
    fun happyPath() {

        // Setup
        val userId = UUID.randomUUID()

        // CreateAccessToken
        val accessToken0Rep = AccessTokenRepFixtures[0].complete(this, userId, 0)
        piperTest.setup(
            endpointConfig = CreateAccessToken.endpointConfig,
            pathParams = mapOf(CreateAccessToken.userId to userId)
        )

        // CreateAccessToken
        val accessToken1Rep = AccessTokenRepFixtures[0].complete(this, userId, 2)
        piperTest.setup(
            endpointConfig = CreateAccessToken.endpointConfig,
            pathParams = mapOf(CreateAccessToken.userId to userId)
        )

        // DeleteAccessToken
        piperTest.test(
            endpointConfig = DeleteAccessToken.endpointConfig,
            pathParams = mapOf(
                DeleteAccessToken.userId to userId,
                DeleteAccessToken.accessTokenId to accessToken0Rep.id
            )
        ) {}

        // GetAccessTokensByUserId
        piperTest.test(
            endpointConfig = GetAccessTokensByUserId.endpointConfig,
            pathParams = mapOf(CreateAccessToken.userId to userId)
        ) {
            val actual = objectMapper.readValue<List<AccessTokenRep.Complete>>(response.content!!)
            assertEquals(listOf(accessToken1Rep), actual)
        }
    }
}

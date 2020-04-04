package io.limberapp.backend.module.auth.endpoint.account.accessToken

import io.limberapp.backend.module.auth.rep.accessToken.AccessTokenRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.accessToken.AccessTokenRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class PostAccessTokenTest : ResourceTest() {

    @Test
    fun happyPath() {

        // Setup
        val accountId = UUID.randomUUID()

        // PostAccessToken
        val accessTokenRep = AccessTokenRepFixtures.fixture.complete(this, accountId, 0)
        val accessTokenOneTimeUseRep = AccessTokenRepFixtures.fixture.oneTimeUse(this, accountId, 0)
        piperTest.test(
            endpointConfig = PostAccessToken.endpointConfig,
            pathParams = mapOf(PostAccessToken.accountId to accountId)
        ) {
            val actual = json.parse<AccessTokenRep.OneTimeUse>(response.content!!)
            assertEquals(accessTokenOneTimeUseRep, actual)
        }

        // GetAccessTokensByAccountId
        piperTest.test(
            endpointConfig = GetAccessTokensByAccountId.endpointConfig,
            pathParams = mapOf(GetAccessTokensByAccountId.accountId to accountId)
        ) {
            val actual = json.parse<Set<AccessTokenRep.Complete>>(response.content!!)
            assertEquals(setOf(accessTokenRep), actual)
        }
    }
}

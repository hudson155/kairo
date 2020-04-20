package io.limberapp.backend.module.auth.endpoint.account.accessToken

import io.limberapp.backend.module.auth.api.accessToken.AccessTokenApi
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

        val accountId = UUID.randomUUID()

        piperTest.test(AccessTokenApi.GetByAccountId(accountId)) {
            val actual = json.parseList<AccessTokenRep.Complete>(response.content!!).toSet()
            assertTrue(actual.isEmpty())
        }
    }

    @Test
    fun happyPathSomeExist() {

        val accountId = UUID.randomUUID()

        val accessToken0Rep = AccessTokenRepFixtures.fixture.complete(this, accountId, 0)
        piperTest.setup(AccessTokenApi.Post(accountId))

        val accessToken1Rep = AccessTokenRepFixtures.fixture.complete(this, accountId, 2)
        piperTest.setup(AccessTokenApi.Post(accountId))

        piperTest.test(AccessTokenApi.GetByAccountId(accountId)) {
            val actual = json.parseList<AccessTokenRep.Complete>(response.content!!).toSet()
            assertEquals(setOf(accessToken0Rep, accessToken1Rep), actual)
        }
    }
}

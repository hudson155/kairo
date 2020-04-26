package io.limberapp.backend.module.auth.endpoint.account.accessToken

import io.limberapp.backend.module.auth.api.accessToken.AccessTokenApi
import io.limberapp.backend.module.auth.rep.accessToken.AccessTokenRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.accessToken.AccessTokenRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetAccessTokensByAccountGuidTest : ResourceTest() {
    @Test
    fun happyPathNoneExist() {
        val accountGuid = UUID.randomUUID()

        piperTest.test(AccessTokenApi.GetByAccountGuid(accountGuid)) {
            val actual = json.parseSet<AccessTokenRep.Complete>(response.content!!)
            assertTrue(actual.isEmpty())
        }
    }

    @Test
    fun happyPathSomeExist() {
        val accountGuid = UUID.randomUUID()

        val accessToken0Rep = AccessTokenRepFixtures.fixture.complete(this, accountGuid, 0)
        piperTest.setup(AccessTokenApi.Post(accountGuid))

        val accessToken1Rep = AccessTokenRepFixtures.fixture.complete(this, accountGuid, 2)
        piperTest.setup(AccessTokenApi.Post(accountGuid))

        piperTest.test(AccessTokenApi.GetByAccountGuid(accountGuid)) {
            val actual = json.parseSet<AccessTokenRep.Complete>(response.content!!)
            assertEquals(setOf(accessToken0Rep, accessToken1Rep), actual)
        }
    }
}

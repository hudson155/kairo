package io.limberapp.backend.module.auth.endpoint.account.accessToken

import io.limberapp.backend.module.auth.api.accessToken.AccessTokenApi
import io.limberapp.backend.module.auth.rep.accessToken.AccessTokenRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.accessToken.AccessTokenRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class PostAccessTokenTest : ResourceTest() {

    @Test
    fun happyPath() {

        val accountId = UUID.randomUUID()

        val accessTokenRep = AccessTokenRepFixtures.fixture.complete(this, accountId, 0)
        val accessTokenOneTimeUseRep = AccessTokenRepFixtures.fixture.oneTimeUse(this, accountId, 0)
        piperTest.test(AccessTokenApi.Post(accountId)) {
            val actual = json.parse<AccessTokenRep.OneTimeUse>(response.content!!)
            assertEquals(accessTokenOneTimeUseRep, actual)
        }

        piperTest.test(AccessTokenApi.GetByAccountId(accountId)) {
            val actual = json.parseList<AccessTokenRep.Complete>(response.content!!).toSet()
            assertEquals(setOf(accessTokenRep), actual)
        }
    }
}

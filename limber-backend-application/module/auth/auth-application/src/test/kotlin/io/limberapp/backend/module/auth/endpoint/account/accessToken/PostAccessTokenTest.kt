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
        val accountGuid = UUID.randomUUID()

        val accessTokenRep = AccessTokenRepFixtures.fixture.complete(this, accountGuid, 0)
        val accessTokenOneTimeUseRep = AccessTokenRepFixtures.fixture.oneTimeUse(this, accountGuid, 0)
        piperTest.test(AccessTokenApi.Post(accountGuid)) {
            val actual = json.parse<AccessTokenRep.OneTimeUse>(response.content!!)
            assertEquals(accessTokenOneTimeUseRep, actual)
        }

        piperTest.test(AccessTokenApi.GetByAccountGuid(accountGuid)) {
            val actual = json.parseSet<AccessTokenRep.Complete>(response.content!!)
            assertEquals(setOf(accessTokenRep), actual)
        }
    }
}

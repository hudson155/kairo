package io.limberapp.backend.module.auth.endpoint.account.accessToken

import io.limberapp.backend.module.auth.api.accessToken.AccessTokenApi
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
        val accountId = UUID.randomUUID()
        val accessTokenId = UUID.randomUUID()

        piperTest.test(
            endpoint = AccessTokenApi.Delete(accountId, accessTokenId),
            expectedException = AccessTokenNotFound()
        )
    }

    @Test
    fun happyPath() {
        val accountId = UUID.randomUUID()

        val accessToken0Rep = AccessTokenRepFixtures.fixture.complete(this, accountId, 0)
        piperTest.setup(AccessTokenApi.Post(accountId))

        val accessToken1Rep = AccessTokenRepFixtures.fixture.complete(this, accountId, 2)
        piperTest.setup(AccessTokenApi.Post(accountId))

        piperTest.test(AccessTokenApi.Delete(accountId, accessToken0Rep.id)) {}

        piperTest.test(AccessTokenApi.GetByAccountId(accountId)) {
            val actual = json.parseList<AccessTokenRep.Complete>(response.content!!).toSet()
            assertEquals(setOf(accessToken1Rep), actual)
        }
    }
}

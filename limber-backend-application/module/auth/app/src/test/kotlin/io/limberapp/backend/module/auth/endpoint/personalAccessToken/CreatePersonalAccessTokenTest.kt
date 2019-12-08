package io.limberapp.backend.module.auth.endpoint.personalAccessToken

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.auth.rep.personalAccessToken.PersonalAccessTokenRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.personalAccessToken.PersonalAccessTokenRepFixtures
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class CreatePersonalAccessTokenTest : ResourceTest() {

    @Test
    fun happyPath() {

        // Setup
        val userId = UUID.randomUUID()

        // CreatePersonalAccessToken
        val personalAccessTokenRep = PersonalAccessTokenRepFixtures[0].complete(this, userId, 0)
        val personalAccessTokenOneTimeUseRep = PersonalAccessTokenRepFixtures[0].oneTimeUse(this, userId, 0)
        piperTest.test(
            endpointConfig = CreatePersonalAccessToken.endpointConfig,
            pathParams = mapOf(CreatePersonalAccessToken.userId to userId.toString())
        ) {
            val actual = objectMapper.readValue<PersonalAccessTokenRep.OneTimeUse>(response.content!!)
            assertEquals(personalAccessTokenOneTimeUseRep, actual)
        }

        // GetPersonalAccessTokensByUserId
        piperTest.test(
            endpointConfig = GetPersonalAccessTokensByUserId.endpointConfig,
            pathParams = mapOf(GetPersonalAccessTokensByUserId.userId to userId.toString())
        ) {
            val actual = objectMapper.readValue<List<PersonalAccessTokenRep.Complete>>(response.content!!)
            assertEquals(listOf(personalAccessTokenRep), actual)
        }
    }
}

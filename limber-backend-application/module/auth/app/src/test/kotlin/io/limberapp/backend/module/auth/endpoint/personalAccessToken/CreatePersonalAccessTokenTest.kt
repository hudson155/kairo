package io.limberapp.backend.module.auth.endpoint.personalAccessToken

import com.fasterxml.jackson.module.kotlin.readValue
import com.piperframework.util.uuid.base64Encode
import io.limberapp.backend.module.auth.rep.personalAccessToken.PersonalAccessTokenRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

internal class CreatePersonalAccessTokenTest : ResourceTest() {

    @Test
    fun happyPath() {

        // Setup
        val userId = UUID.randomUUID()

        // CreatePersonalAccessToken
        val personalAccessTokenRep = PersonalAccessTokenRep.Complete(
            id = deterministicUuidGenerator[0],
            created = LocalDateTime.now(fixedClock),
            userId = userId
        )
        val personalAccessTokenValue = deterministicUuidGenerator[1].base64Encode()
        piperTest.test(
            endpointConfig = CreatePersonalAccessToken.endpointConfig,
            pathParams = mapOf(CreatePersonalAccessToken.userId to userId.toString())
        ) {
            val actual = objectMapper.readValue<PersonalAccessTokenRep.OneTimeUse>(response.content!!)
            val expected = PersonalAccessTokenRep.OneTimeUse(
                id = personalAccessTokenRep.id,
                created = personalAccessTokenRep.created,
                userId = personalAccessTokenRep.userId,
                token = personalAccessTokenValue
            )
            assertEquals(expected, actual)
        }

        // GetPersonalAccessTokensByUserId
        piperTest.test(
            endpointConfig = GetPersonalAccessTokensByUserId.endpointConfig,
            pathParams = mapOf(GetPersonalAccessTokensByUserId.userId to userId.toString())
        ) {
            val actual = objectMapper.readValue<List<PersonalAccessTokenRep.Complete>>(response.content!!)
            val expected = listOf(personalAccessTokenRep)
            assertEquals(expected, actual)
        }
    }
}

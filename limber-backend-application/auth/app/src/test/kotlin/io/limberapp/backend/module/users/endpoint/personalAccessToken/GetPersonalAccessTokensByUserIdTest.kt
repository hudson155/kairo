package io.limberapp.backend.module.users.endpoint.personalAccessToken

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.auth.endpoint.personalAccessToken.CreatePersonalAccessToken
import io.limberapp.backend.module.auth.endpoint.personalAccessToken.GetPersonalAccessTokensByUserId
import io.limberapp.backend.module.auth.rep.personalAccessToken.PersonalAccessTokenRep
import io.limberapp.backend.module.users.testing.ResourceTest
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetPersonalAccessTokensByUserIdTest : ResourceTest() {

    @Test
    fun getNone() {
        limberTest.test(
            endpointConfig = GetPersonalAccessTokensByUserId.endpointConfig,
            pathParams = mapOf(CreatePersonalAccessToken.userId to UUID.randomUUID().toString())
        ) {
            val actual = objectMapper.readValue<List<PersonalAccessTokenRep.Complete>>(response.content!!)
            assertTrue(actual.isEmpty())
        }
    }

    @Test
    fun getExisting() {

        val userId = UUID.randomUUID()
        val id = deterministicUuidGenerator[0]
        limberTest.test(
            endpointConfig = CreatePersonalAccessToken.endpointConfig,
            pathParams = mapOf(CreatePersonalAccessToken.userId to userId.toString())
        ) {}

        limberTest.test(
            endpointConfig = GetPersonalAccessTokensByUserId.endpointConfig,
            pathParams = mapOf(CreatePersonalAccessToken.userId to userId.toString())
        ) {
            val actual = objectMapper.readValue<List<PersonalAccessTokenRep.Complete>>(response.content!!)
            val expected = listOf(
                PersonalAccessTokenRep.Complete(
                    id = id,
                    created = LocalDateTime.now(fixedClock),
                    userId = userId
                )
            )
            assertEquals(expected, actual)
        }
    }
}

package io.limberapp.backend.module.auth.endpoint.personalAccessToken

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.auth.rep.personalAccessToken.PersonalAccessTokenRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import com.piperframework.util.uuid.base64Encode
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

internal class CreatePersonalAccessTokenTest : ResourceTest() {

    @Test
    fun create() {
        val userId = UUID.randomUUID()
        val id = deterministicUuidGenerator[0]
        limberTest.test(
            endpointConfig = CreatePersonalAccessToken.endpointConfig,
            pathParams = mapOf(CreatePersonalAccessToken.userId to userId.toString())
        ) {
            val actual = objectMapper.readValue<PersonalAccessTokenRep.OneTimeUse>(response.content!!)
            val expected = PersonalAccessTokenRep.OneTimeUse(
                id = id,
                created = LocalDateTime.now(fixedClock),
                userId = userId,
                token = deterministicUuidGenerator[1].base64Encode()
            )
            assertEquals(expected, actual)
        }
    }
}

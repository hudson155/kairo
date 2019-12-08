package io.limberapp.backend.module.auth.testing.fixtures.personalAccessToken

import com.piperframework.util.uuid.base64Encode
import io.limberapp.backend.module.auth.rep.personalAccessToken.PersonalAccessTokenRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import java.time.LocalDateTime
import java.util.UUID

internal object PersonalAccessTokenRepFixtures {

    data class Fixture(
        val oneTimeUse: ResourceTest.(userId: UUID, idSeed: Int) -> PersonalAccessTokenRep.OneTimeUse,
        val complete: ResourceTest.(userId: UUID, idSeed: Int) -> PersonalAccessTokenRep.Complete
    )

    operator fun get(i: Int) = fixtures[i]

    private val fixtures = listOf(
        Fixture({ userId, idSeed ->
            PersonalAccessTokenRep.OneTimeUse(
                id = deterministicUuidGenerator[idSeed],
                created = LocalDateTime.now(fixedClock),
                userId = userId,
                token = deterministicUuidGenerator[idSeed + 1].base64Encode()
            )
        }, { userId, idSeed ->
            PersonalAccessTokenRep.Complete(
                id = deterministicUuidGenerator[idSeed],
                created = LocalDateTime.now(fixedClock),
                userId = userId
            )
        })
    )
}

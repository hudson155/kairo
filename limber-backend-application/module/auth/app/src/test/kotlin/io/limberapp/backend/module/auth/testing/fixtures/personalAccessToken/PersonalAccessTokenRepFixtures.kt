package io.limberapp.backend.module.auth.testing.fixtures.personalAccessToken

import com.piperframework.util.uuid.base64Encode
import io.limberapp.backend.module.auth.rep.personalAccessToken.PersonalAccessTokenRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import java.time.LocalDateTime
import java.util.UUID

internal object PersonalAccessTokenRepFixtures {

    val OneTimeUse = listOf(
        fun ResourceTest.(userId: UUID, idSeed: Int): PersonalAccessTokenRep.OneTimeUse {
            return PersonalAccessTokenRep.OneTimeUse(
                id = deterministicUuidGenerator[idSeed],
                created = LocalDateTime.now(fixedClock),
                userId = userId,
                token = deterministicUuidGenerator[idSeed + 1].base64Encode()
            )
        }
    )

    val Complete = OneTimeUse.map { rep ->
        fun ResourceTest.(userId: UUID, idSeed: Int): PersonalAccessTokenRep.Complete {
            return PersonalAccessTokenRep.Complete(
                id = deterministicUuidGenerator[idSeed],
                created = LocalDateTime.now(fixedClock),
                userId = userId
            )
        }
    }
}

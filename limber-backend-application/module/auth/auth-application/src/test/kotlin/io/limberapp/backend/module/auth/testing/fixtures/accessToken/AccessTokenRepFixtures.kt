package io.limberapp.backend.module.auth.testing.fixtures.accessToken

import com.piperframework.util.uuid.base64Encode
import io.limberapp.backend.module.auth.rep.accessToken.AccessTokenRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import java.time.LocalDateTime
import java.util.UUID

internal object AccessTokenRepFixtures {
    data class Fixture(
        val oneTimeUse: ResourceTest.(userGuid: UUID, idSeed: Int) -> AccessTokenRep.OneTimeUse,
        val complete: ResourceTest.(userGuid: UUID, idSeed: Int) -> AccessTokenRep.Complete
    )

    val fixture = Fixture({ userGuid, idSeed ->
        AccessTokenRep.OneTimeUse(
            guid = deterministicUuidGenerator[idSeed],
            createdDate = LocalDateTime.now(fixedClock),
            userGuid = userGuid,
            token = deterministicUuidGenerator[idSeed].base64Encode().dropLast(2)
                    + deterministicUuidGenerator[idSeed + 1].base64Encode().dropLast(2)
        )
    }, { userGuid, idSeed ->
        AccessTokenRep.Complete(
            guid = deterministicUuidGenerator[idSeed],
            createdDate = LocalDateTime.now(fixedClock),
            userGuid = userGuid
        )
    })
}

package io.limberapp.backend.module.auth.mapper.accessToken

import com.google.inject.Inject
import com.piperframework.util.uuid.base64Encode
import com.piperframework.util.uuid.uuidGenerator.UuidGenerator
import io.limberapp.backend.module.auth.model.accessToken.AccessTokenModel
import io.limberapp.backend.module.auth.rep.accessToken.AccessTokenRep
import java.time.Clock
import java.time.LocalDateTime
import java.util.UUID

internal class AccessTokenMapper @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator
) {

    fun model(userId: UUID) = AccessTokenModel(
        id = uuidGenerator.generate(),
        created = LocalDateTime.now(clock),
        userId = userId,
        token = uuidGenerator.generate().base64Encode()
    )

    fun oneTimeUseRep(model: AccessTokenModel) = AccessTokenRep.OneTimeUse(
        id = model.id,
        created = model.created,
        userId = model.userId,
        token = model.token
    )

    fun completeRep(model: AccessTokenModel) = AccessTokenRep.Complete(
        id = model.id,
        created = model.created,
        userId = model.userId
    )
}

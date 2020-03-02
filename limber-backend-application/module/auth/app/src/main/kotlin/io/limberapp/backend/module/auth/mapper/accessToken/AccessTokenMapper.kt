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

    fun model(userId: UUID): Pair<AccessTokenModel, UUID> {
        val id = uuidGenerator.generate()
        val rawSecretAsUuid = uuidGenerator.generate()
        val model = AccessTokenModel(
            id = id,
            created = LocalDateTime.now(clock),
            userId = userId,
            encryptedSecret = rawSecretAsUuid.base64Encode().dropLast(2)
        )
        return Pair(model, rawSecretAsUuid)
    }

    fun oneTimeUseRep(model: AccessTokenModel, rawSecretAsUuid: UUID) = AccessTokenRep.OneTimeUse(
        id = model.id,
        created = model.created,
        userId = model.userId,
        token = model.id.base64Encode().dropLast(2) + rawSecretAsUuid.base64Encode().dropLast(2)
    )

    fun completeRep(model: AccessTokenModel) = AccessTokenRep.Complete(
        id = model.id,
        created = model.created,
        userId = model.userId
    )
}

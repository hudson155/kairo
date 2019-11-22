package io.limberapp.backend.module.auth.mapper.api.personalAccessToken

import com.google.inject.Inject
import io.limberapp.backend.module.auth.model.personalAccessToken.PersonalAccessTokenModel
import io.limberapp.backend.module.auth.rep.personalAccessToken.PersonalAccessTokenRep
import io.limberapp.framework.util.uuid.base64Encode
import io.limberapp.framework.util.uuid.uuidGenerator.UuidGenerator
import java.time.Clock
import java.time.LocalDateTime
import java.util.UUID

internal class PersonalAccessTokenMapper @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator
) {

    fun model(userId: UUID) = PersonalAccessTokenModel(
        id = uuidGenerator.generate(),
        created = LocalDateTime.now(clock),
        version = 0,
        userId = userId,
        token = uuidGenerator.generate().base64Encode()
    )

    fun oneTimeUseRep(model: PersonalAccessTokenModel) = PersonalAccessTokenRep.OneTimeUse(
        id = model.id,
        created = model.created,
        userId = model.userId,
        token = model.token
    )

    fun completeRep(model: PersonalAccessTokenModel) = PersonalAccessTokenRep.Complete(
        id = model.id,
        created = model.created,
        userId = model.userId
    )
}

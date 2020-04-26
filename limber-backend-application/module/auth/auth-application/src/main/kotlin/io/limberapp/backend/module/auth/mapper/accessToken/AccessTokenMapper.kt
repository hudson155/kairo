package io.limberapp.backend.module.auth.mapper.accessToken

import com.google.inject.Inject
import com.piperframework.config.hashing.HashingConfig
import com.piperframework.util.uuid.UuidGenerator
import com.piperframework.util.uuid.base64Encode
import io.limberapp.backend.module.auth.model.accessToken.AccessTokenModel
import io.limberapp.backend.module.auth.rep.accessToken.AccessTokenRep
import org.mindrot.jbcrypt.BCrypt
import java.time.Clock
import java.time.LocalDateTime
import java.util.UUID

internal class AccessTokenMapper @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator,
    private val hashingConfig: HashingConfig
) {
    fun model(accountGuid: UUID): Pair<AccessTokenModel, UUID> {
        val guid = uuidGenerator.generate()
        val rawSecretAsUuid = uuidGenerator.generate()
        val model = AccessTokenModel(
            guid = guid,
            createdDate = LocalDateTime.now(clock),
            accountGuid = accountGuid,
            encryptedSecret = run {
                val salt = BCrypt.gensalt(hashingConfig.logRounds)
                return@run BCrypt.hashpw(rawSecretAsUuid.base64Encode().dropLast(2), salt)
            }
        )
        return Pair(model, rawSecretAsUuid)
    }

    fun oneTimeUseRep(model: AccessTokenModel, rawSecretAsUuid: UUID) = AccessTokenRep.OneTimeUse(
        guid = model.guid,
        createdDate = model.createdDate,
        accountGuid = model.accountGuid,
        token = model.guid.base64Encode().dropLast(2) + rawSecretAsUuid.base64Encode().dropLast(2)
    )

    fun completeRep(model: AccessTokenModel) = AccessTokenRep.Complete(
        guid = model.guid,
        createdDate = model.createdDate,
        accountGuid = model.accountGuid
    )
}

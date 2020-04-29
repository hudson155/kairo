package io.limberapp.backend.module.auth.service.accessToken

import com.google.inject.Inject
import io.limberapp.backend.module.auth.exception.accessToken.AccessTokenNotFound
import io.limberapp.backend.module.auth.model.accessToken.AccessTokenModel
import io.limberapp.backend.module.auth.store.accessToken.AccessTokenStore
import org.mindrot.jbcrypt.BCrypt
import java.util.UUID

internal class AccessTokenServiceImpl @Inject constructor(
    private val accessTokenStore: AccessTokenStore
) : AccessTokenService {
    override fun create(model: AccessTokenModel) = accessTokenStore.create(model)

    override fun getIfValid(accessTokenGuid: UUID, accessTokenSecret: String): AccessTokenModel? {
        val model = accessTokenStore.get(accessTokenGuid) ?: return null
        if (!BCrypt.checkpw(accessTokenSecret, model.encryptedSecret)) return null
        return model
    }

    override fun getByAccountGuid(accountGuid: UUID) = accessTokenStore.getByAccountGuid(accountGuid)

    override fun delete(accountGuid: UUID, accessTokenGuid: UUID) {
        if (accessTokenStore.get(accessTokenGuid)?.accountGuid != accountGuid) throw AccessTokenNotFound()
        accessTokenStore.delete(accessTokenGuid)
    }
}

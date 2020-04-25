package io.limberapp.backend.module.auth.service.accessToken

import io.limberapp.backend.module.auth.model.accessToken.AccessTokenModel
import java.util.UUID

interface AccessTokenService {
    fun create(model: AccessTokenModel)

    fun getIfValid(accessTokenGuid: UUID, accessTokenSecret: String): AccessTokenModel?

    fun getByAccountGuid(userGuid: UUID): Set<AccessTokenModel>

    fun delete(userGuid: UUID, accessTokenGuid: UUID)
}

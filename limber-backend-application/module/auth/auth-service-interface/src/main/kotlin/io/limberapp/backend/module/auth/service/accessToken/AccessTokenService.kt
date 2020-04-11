package io.limberapp.backend.module.auth.service.accessToken

import io.limberapp.backend.module.auth.model.accessToken.AccessTokenModel
import java.util.UUID

interface AccessTokenService {

    fun create(model: AccessTokenModel)

    fun getIfValid(accessTokenId: UUID, accessTokenSecret: String): AccessTokenModel?

    fun getByAccountId(userId: UUID): Set<AccessTokenModel>

    fun delete(userId: UUID, accessTokenId: UUID)
}

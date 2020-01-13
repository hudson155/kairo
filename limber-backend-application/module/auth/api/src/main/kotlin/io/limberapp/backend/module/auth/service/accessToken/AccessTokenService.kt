package io.limberapp.backend.module.auth.service.accessToken

import io.limberapp.backend.module.auth.model.accessToken.AccessTokenModel
import java.util.UUID

interface AccessTokenService {

    fun create(model: AccessTokenModel)

    fun getByToken(token: String): AccessTokenModel?

    fun getByUserId(userId: UUID): List<AccessTokenModel>

    fun delete(userId: UUID, accessTokenId: UUID)
}

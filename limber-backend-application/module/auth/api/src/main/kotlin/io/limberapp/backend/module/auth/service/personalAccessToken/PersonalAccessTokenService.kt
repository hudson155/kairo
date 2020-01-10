package io.limberapp.backend.module.auth.service.personalAccessToken

import io.limberapp.backend.module.auth.model.personalAccessToken.PersonalAccessTokenModel
import java.util.UUID

interface PersonalAccessTokenService {

    fun create(model: PersonalAccessTokenModel)

    fun getByToken(token: String): PersonalAccessTokenModel?

    fun getByUserId(userId: UUID): List<PersonalAccessTokenModel>

    fun delete(userId: UUID, personalAccessTokenId: UUID)
}

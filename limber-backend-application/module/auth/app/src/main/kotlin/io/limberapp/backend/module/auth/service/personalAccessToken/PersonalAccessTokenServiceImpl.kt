package io.limberapp.backend.module.auth.service.personalAccessToken

import com.google.inject.Inject
import com.piperframework.module.annotation.Store
import io.limberapp.backend.module.auth.model.personalAccessToken.PersonalAccessTokenModel
import java.util.UUID

internal class PersonalAccessTokenServiceImpl @Inject constructor(
    @Store private val personalAccessTokenStore: PersonalAccessTokenService
) : PersonalAccessTokenService {

    override fun create(model: PersonalAccessTokenModel) = personalAccessTokenStore.create(model)

    override fun getByToken(token: String) = personalAccessTokenStore.getByToken(token)

    override fun getByUserId(userId: UUID) = personalAccessTokenStore.getByUserId(userId)

    override fun delete(userId: UUID, personalAccessTokenId: UUID) =
        personalAccessTokenStore.delete(userId, personalAccessTokenId)
}

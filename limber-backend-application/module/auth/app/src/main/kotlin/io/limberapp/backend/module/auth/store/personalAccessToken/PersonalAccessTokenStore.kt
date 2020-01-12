package io.limberapp.backend.module.auth.store.personalAccessToken

import com.piperframework.store.Store
import io.limberapp.backend.module.auth.model.personalAccessToken.PersonalAccessTokenModel
import java.util.UUID

internal interface PersonalAccessTokenStore : Store {

    fun create(model: PersonalAccessTokenModel)

    fun get(userId: UUID, personalAccessTokenId: UUID): PersonalAccessTokenModel?

    fun getByToken(token: String): PersonalAccessTokenModel?

    fun getByUserId(userId: UUID): List<PersonalAccessTokenModel>

    fun delete(userId: UUID, personalAccessTokenId: UUID)
}

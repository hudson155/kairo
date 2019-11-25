package io.limberapp.backend.module.auth.store.personalAccessToken

import com.piperframework.store.Store
import io.limberapp.backend.module.auth.entity.personalAccessToken.PersonalAccessTokenEntity
import java.util.UUID

internal interface PersonalAccessTokenStore : Store<PersonalAccessTokenEntity> {

    fun create(entity: PersonalAccessTokenEntity)

    fun getByToken(token: String): PersonalAccessTokenEntity?

    fun getByUserId(userId: UUID): List<PersonalAccessTokenEntity>

    fun delete(userId: UUID, id: UUID): Unit?
}

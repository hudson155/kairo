package io.limberapp.backend.module.auth.store.personalAccessToken

import io.limberapp.backend.module.auth.entity.personalAccessToken.PersonalAccessTokenEntity
import com.piperframework.store.Store
import java.util.UUID

internal interface PersonalAccessTokenStore : Store<PersonalAccessTokenEntity> {

    fun create(entity: PersonalAccessTokenEntity)

    fun getByUserId(userId: UUID): List<PersonalAccessTokenEntity>

    fun delete(userId: UUID, id: UUID): Unit?
}

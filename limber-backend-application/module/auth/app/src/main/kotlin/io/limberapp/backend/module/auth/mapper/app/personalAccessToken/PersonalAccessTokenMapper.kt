package io.limberapp.backend.module.auth.mapper.app.personalAccessToken

import com.google.inject.Inject
import io.limberapp.backend.module.auth.entity.personalAccessToken.PersonalAccessTokenEntity
import io.limberapp.backend.module.auth.model.personalAccessToken.PersonalAccessTokenModel

internal class PersonalAccessTokenMapper @Inject constructor() {

    fun entity(model: PersonalAccessTokenModel) = PersonalAccessTokenEntity(
        id = model.id,
        created = model.created,
        userId = model.userId,
        token = model.token
    )

    fun model(entity: PersonalAccessTokenEntity) = PersonalAccessTokenModel(
        id = entity.id,
        created = entity.created,
        userId = entity.userId,
        token = entity.token
    )
}

package io.limberapp.backend.module.auth.service.personalAccessToken

import com.google.inject.Inject
import io.limberapp.backend.module.auth.exception.notFound.PersonalAccessTokenNotFound
import io.limberapp.backend.module.auth.mapper.app.personalAccessToken.PersonalAccessTokenMapper
import io.limberapp.backend.module.auth.model.personalAccessToken.PersonalAccessTokenModel
import io.limberapp.backend.module.auth.store.personalAccessToken.MongoPersonalAccessTokenStore
import java.util.UUID

internal class PersonalAccessTokenServiceImpl @Inject constructor(
    private val personalAccessTokenStore: MongoPersonalAccessTokenStore,
    private val personalAccessTokenMapper: PersonalAccessTokenMapper
) : PersonalAccessTokenService {

    override fun create(model: PersonalAccessTokenModel) {
        val entity = personalAccessTokenMapper.entity(model)
        personalAccessTokenStore.create(entity)
    }

    override fun getByToken(token: String): PersonalAccessTokenModel? {
        val entity = personalAccessTokenStore.getByToken(token) ?: return null
        return personalAccessTokenMapper.model(entity)
    }

    override fun getByUserId(userId: UUID): List<PersonalAccessTokenModel> {
        val entities = personalAccessTokenStore.getByUserId(userId)
        return entities.map { personalAccessTokenMapper.model(it) }
    }

    override fun delete(userId: UUID, id: UUID) {
        personalAccessTokenStore.delete(userId, id) ?: throw PersonalAccessTokenNotFound()
    }
}

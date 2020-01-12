package io.limberapp.backend.module.auth.store.personalAccessToken

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import com.piperframework.store.MongoCollection
import com.piperframework.store.MongoIndex
import com.piperframework.store.MongoStore
import io.limberapp.backend.module.auth.entity.personalAccessToken.PersonalAccessTokenEntity
import io.limberapp.backend.module.auth.exception.notFound.PersonalAccessTokenNotFound
import io.limberapp.backend.module.auth.mapper.app.personalAccessToken.PersonalAccessTokenMapper
import io.limberapp.backend.module.auth.model.personalAccessToken.PersonalAccessTokenModel
import org.litote.kmongo.and
import org.litote.kmongo.ascending
import org.litote.kmongo.eq
import java.util.UUID

internal class MongoPersonalAccessTokenStore @Inject constructor(
    mongoDatabase: MongoDatabase,
    private val personalAccessTokenMapper: PersonalAccessTokenMapper
) : PersonalAccessTokenStore, MongoStore<PersonalAccessTokenEntity>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = PersonalAccessTokenEntity.name,
        clazz = PersonalAccessTokenEntity::class
    ),
    indices = listOf<MongoIndex<PersonalAccessTokenEntity>>(
        { ensureIndex(ascending(PersonalAccessTokenEntity::userId), unique = false) },
        { ensureIndex(ascending(PersonalAccessTokenEntity::token), unique = true) }
    )
) {

    override fun create(model: PersonalAccessTokenModel) {
        val entity = personalAccessTokenMapper.entity(model)
        collection.insertOne(entity)
    }

    override fun get(userId: UUID, personalAccessTokenId: UUID): PersonalAccessTokenModel? {
        val entity = collection.findOne(
            filter = and(
                PersonalAccessTokenEntity::userId eq userId,
                PersonalAccessTokenEntity::id eq personalAccessTokenId
            )
        ) ?: return null
        return personalAccessTokenMapper.model(entity)
    }

    override fun getByToken(token: String): PersonalAccessTokenModel? {
        val entity = collection.findOne(PersonalAccessTokenEntity::token eq token) ?: return null
        return personalAccessTokenMapper.model(entity)
    }

    override fun getByUserId(userId: UUID): List<PersonalAccessTokenModel> {
        val entities = collection.find(PersonalAccessTokenEntity::userId eq userId)
        return entities.map { personalAccessTokenMapper.model(it) }
    }

    override fun delete(userId: UUID, personalAccessTokenId: UUID) {
        collection.findOneAndDelete(
            filter = and(
                PersonalAccessTokenEntity::userId eq userId,
                PersonalAccessTokenEntity::id eq personalAccessTokenId
            )
        ) ?: throw PersonalAccessTokenNotFound()
    }
}

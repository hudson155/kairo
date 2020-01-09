package io.limberapp.backend.module.auth.store.personalAccessToken

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import com.piperframework.store.MongoCollection
import com.piperframework.store.MongoStore
import io.limberapp.backend.module.auth.entity.personalAccessToken.PersonalAccessTokenEntity
import io.limberapp.backend.module.auth.exception.notFound.PersonalAccessTokenNotFound
import org.litote.kmongo.and
import org.litote.kmongo.ascending
import org.litote.kmongo.eq
import java.util.UUID

internal class MongoPersonalAccessTokenStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : PersonalAccessTokenStore, MongoStore<PersonalAccessTokenEntity>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = PersonalAccessTokenEntity.name,
        clazz = PersonalAccessTokenEntity::class
    ),
    indices = listOf(
        { ensureIndex(ascending(PersonalAccessTokenEntity::userId), unique = false) },
        { ensureIndex(ascending(PersonalAccessTokenEntity::token), unique = true) }
    )
) {

    override fun create(entity: PersonalAccessTokenEntity) {
        collection.insertOne(entity)
    }

    override fun get(userId: UUID, personalAccessTokenId: UUID) = collection.findOne(
        filter = and(
            PersonalAccessTokenEntity::userId eq userId,
            PersonalAccessTokenEntity::id eq personalAccessTokenId
        )
    )

    override fun getByToken(token: String) = collection.findOne(PersonalAccessTokenEntity::token eq token)

    override fun getByUserId(userId: UUID) = collection.find(PersonalAccessTokenEntity::userId eq userId)

    override fun delete(userId: UUID, personalAccessTokenId: UUID) {
        collection.findOneAndDelete(
            filter = and(
                PersonalAccessTokenEntity::userId eq userId,
                PersonalAccessTokenEntity::id eq personalAccessTokenId
            )
        ) ?: throw PersonalAccessTokenNotFound()
    }
}

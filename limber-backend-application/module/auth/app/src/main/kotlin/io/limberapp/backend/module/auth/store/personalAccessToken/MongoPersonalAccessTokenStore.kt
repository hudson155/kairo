package io.limberapp.backend.module.auth.store.personalAccessToken

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import com.piperframework.store.MongoCollection
import com.piperframework.store.MongoIndex
import com.piperframework.store.MongoStore
import io.limberapp.backend.module.auth.entity.personalAccessToken.PersonalAccessTokenEntity
import org.litote.kmongo.and
import org.litote.kmongo.ascending
import org.litote.kmongo.eq
import java.util.UUID

internal class MongoPersonalAccessTokenStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : PersonalAccessTokenStore, MongoStore<PersonalAccessTokenEntity>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = PersonalAccessTokenEntity.collectionName,
        clazz = PersonalAccessTokenEntity::class
    ),
    indices = listOf<MongoIndex<PersonalAccessTokenEntity>> {
        ensureIndex(ascending(PersonalAccessTokenEntity::userId), unique = false)
    }
) {

    override fun create(entity: PersonalAccessTokenEntity) {
        collection.insertOne(entity)
    }

    override fun getByToken(token: String) =
        collection.findOne(PersonalAccessTokenEntity::token eq token)

    override fun getByUserId(userId: UUID) =
        collection.find(PersonalAccessTokenEntity::userId eq userId)

    override fun delete(userId: UUID, id: UUID) = collection.findOneAndDelete(
        filter = and(PersonalAccessTokenEntity::id eq id, PersonalAccessTokenEntity::userId eq userId)
    )
}

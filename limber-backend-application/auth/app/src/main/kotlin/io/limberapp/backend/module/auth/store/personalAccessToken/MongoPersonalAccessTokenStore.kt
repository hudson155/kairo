package io.limberapp.backend.module.auth.store.personalAccessToken

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import io.limberapp.backend.module.auth.entity.personalAccessToken.PersonalAccessTokenEntity
import io.limberapp.framework.store.MongoCollection
import io.limberapp.framework.store.MongoCrStore
import org.litote.kmongo.and
import org.litote.kmongo.ascending
import org.litote.kmongo.eq
import java.util.UUID

internal class MongoPersonalAccessTokenStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : PersonalAccessTokenStore, MongoCrStore<PersonalAccessTokenEntity>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = PersonalAccessTokenEntity.collectionName,
        clazz = PersonalAccessTokenEntity::class
    ),
    indices = listOf<MongoCollection<PersonalAccessTokenEntity>.() -> Unit> {
        ensureIndex(ascending(PersonalAccessTokenEntity::userId), unique = false)
    }
) {

    override fun getByUserId(userId: UUID) =
        collection.find(PersonalAccessTokenEntity::userId eq userId)

    override fun delete(userId: UUID, id: UUID) = collection.findOneAndDelete(
        filter = and(
            PersonalAccessTokenEntity::id eq id,
            PersonalAccessTokenEntity::userId eq userId
        )
    )
}

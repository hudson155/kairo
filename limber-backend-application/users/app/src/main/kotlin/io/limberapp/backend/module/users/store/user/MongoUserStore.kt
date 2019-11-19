package io.limberapp.backend.module.users.store.user

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import io.limberapp.backend.module.users.entity.user.UserEntity
import io.limberapp.framework.store.MongoCollection
import io.limberapp.framework.store.MongoStore
import org.litote.kmongo.ascending
import org.litote.kmongo.eq

internal class MongoUserStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : UserStore, MongoStore<UserEntity, UserEntity.Update>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = UserEntity.collectionName,
        clazz = UserEntity::class
    ),
    indices = listOf(
        { ensureIndex(ascending(UserEntity::emailAddress), unique = true) }
    )
) {

    override fun getByEmailAddress(emailAddress: String) =
        collection.findOne(UserEntity::emailAddress eq emailAddress)
}

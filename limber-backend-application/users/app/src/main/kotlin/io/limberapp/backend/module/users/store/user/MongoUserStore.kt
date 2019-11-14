package io.limberapp.backend.module.users.store.user

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import io.limberapp.backend.module.users.entity.user.UserEntity
import io.limberapp.framework.mongo.collection.MongoStoreCollection
import io.limberapp.framework.store.MongoStore

internal class MongoUserStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : UserStore, MongoStore<UserEntity.Creation, UserEntity.Complete, UserEntity.Update>(
    collection = MongoStoreCollection(mongoDatabase, collectionName)
) {

    companion object {
        const val collectionName = "User"
    }
}

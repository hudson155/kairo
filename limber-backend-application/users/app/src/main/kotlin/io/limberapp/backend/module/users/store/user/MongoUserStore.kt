package io.limberapp.backend.module.users.store.user

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import io.limberapp.backend.module.users.entity.user.UserEntity
import io.limberapp.framework.store.MongoCollection
import io.limberapp.framework.store.MongoStore
import org.litote.kmongo.eq

internal class MongoUserStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : UserStore, MongoStore<UserEntity.Complete, UserEntity.Update>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = UserEntity.collectionName,
        clazz = UserEntity.Complete::class
    )
) {

    override fun getByEmailAddress(emailAddress: String): UserEntity.Complete? {
        return collection.findOne(UserEntity.Complete::emailAddress eq emailAddress)
    }
}

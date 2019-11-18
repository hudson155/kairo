package io.limberapp.backend.module.users.store.user

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import io.limberapp.backend.module.users.entity.user.UserEntity
import io.limberapp.framework.store.MongoStore
import org.litote.kmongo.eq
import org.litote.kmongo.findOne

internal class MongoUserStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : UserStore, MongoStore<UserEntity.Complete, UserEntity.Update>(
    collection = mongoDatabase.getCollection(
        UserEntity.collectionName,
        UserEntity.Complete::class.java
    )
) {

    override fun getByEmailAddress(emailAddress: String): UserEntity.Complete? {
        return collection.findOne(UserEntity.Complete::emailAddress eq emailAddress)
    }
}

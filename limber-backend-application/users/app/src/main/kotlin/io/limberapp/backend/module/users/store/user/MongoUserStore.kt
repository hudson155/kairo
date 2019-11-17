package io.limberapp.backend.module.users.store.user

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import io.limberapp.backend.module.users.entity.user.UserEntity
import io.limberapp.framework.mongo.collection.FindFilter
import io.limberapp.framework.mongo.collection.MongoStoreCollection
import io.limberapp.framework.store.MongoStore

internal class MongoUserStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : UserStore, MongoStore<UserEntity.Creation, UserEntity.Complete, UserEntity.Update>(
    collection = MongoStoreCollection(mongoDatabase, collectionName)
) {

    override fun getByEmailAddress(emailAddress: String): UserEntity.Complete? {
        val findFilter = FindFilter().apply {
            eq[UserEntity.Complete::emailAddress.name] = emailAddress
        }
        val document = collection.findOne(findFilter) ?: return null
        return objectMapper.readValue(document.toJson())
    }

    companion object {
        const val collectionName = "User"
    }
}

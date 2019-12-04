package io.limberapp.backend.module.users.store.user

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import com.piperframework.store.MongoCollection
import com.piperframework.store.MongoIndex
import com.piperframework.store.MongoStore
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.entity.user.UserEntity
import org.litote.kmongo.and
import org.litote.kmongo.ascending
import org.litote.kmongo.contains
import org.litote.kmongo.eq
import org.litote.kmongo.not
import org.litote.kmongo.pull
import org.litote.kmongo.push
import java.util.UUID

internal class MongoUserStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : UserStore, MongoStore<UserEntity>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = UserEntity.collectionName,
        clazz = UserEntity::class
    ),
    indices = listOf<MongoIndex<UserEntity>> {
        ensureIndex(ascending(UserEntity::emailAddress), unique = true)
    }
) {

    override fun create(entity: UserEntity) {
        collection.insertOne(entity)
    }

    override fun get(id: UUID) = collection.findOneById(id)

    override fun getByEmailAddress(emailAddress: String) =
        collection.findOne(UserEntity::emailAddress eq emailAddress)

    override fun update(id: UUID, update: UserEntity.Update) =
        collection.findOneByIdAndUpdate(id, update)

    override fun addRole(userId: UUID, roleName: JwtRole): Unit? {
        collection.findOneAndUpdate(
            filter = and(UserEntity::id eq userId, not(UserEntity::roles contains roleName)),
            update = push(UserEntity::roles, roleName)
        ) ?: return null
        return Unit
    }

    override fun removeRole(userId: UUID, roleName: JwtRole): Unit? {
        collection.findOneAndUpdate(
            filter = and(UserEntity::id eq userId, UserEntity::roles contains roleName),
            update = pull(UserEntity::roles, roleName)
        ) ?: return null
        return Unit
    }

    override fun delete(id: UUID) = collection.findOneByIdAndDelete(id)
}

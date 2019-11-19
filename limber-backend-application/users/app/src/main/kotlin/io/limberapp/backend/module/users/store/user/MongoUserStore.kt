package io.limberapp.backend.module.users.store.user

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import io.limberapp.backend.module.users.entity.user.UserEntity
import io.limberapp.framework.endpoint.authorization.jwt.JwtRole
import io.limberapp.framework.store.MongoCollection
import io.limberapp.framework.store.MongoStore
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
) : UserStore, MongoStore<UserEntity, UserEntity.Update>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = UserEntity.collectionName,
        clazz = UserEntity::class
    ),
    indices = listOf<MongoCollection<UserEntity>.() -> Unit> {
        ensureIndex(ascending(UserEntity::emailAddress), unique = true)
    }
) {

    override fun getByEmailAddress(emailAddress: String) =
        collection.findOne(UserEntity::emailAddress eq emailAddress)

    override fun addRole(userId: UUID, roleName: JwtRole): Unit? {
        return collection.findOneAndUpdate(
            filter = and(UserEntity::id eq userId, not(UserEntity::roles contains roleName)),
            update = push(UserEntity::roles, roleName)
        )?.let {}
    }

    override fun removeRole(userId: UUID, roleName: JwtRole): Unit? {
        return collection.findOneAndUpdate(
            filter = and(UserEntity::id eq userId, UserEntity::roles contains roleName),
            update = pull(UserEntity::roles, roleName)
        )?.let {}
    }
}

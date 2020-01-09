package io.limberapp.backend.module.users.store.user

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import com.piperframework.store.MongoCollection
import com.piperframework.store.MongoStore
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.entity.user.UserEntity
import io.limberapp.backend.module.users.exception.conflict.EmailAddressAlreadyTaken
import io.limberapp.backend.module.users.exception.conflict.UserAlreadyHasRole
import io.limberapp.backend.module.users.exception.conflict.UserDoesNotHaveRole
import io.limberapp.backend.module.users.exception.notFound.UserNotFound
import org.bson.conversions.Bson
import org.litote.kmongo.and
import org.litote.kmongo.ascending
import org.litote.kmongo.combine
import org.litote.kmongo.contains
import org.litote.kmongo.eq
import org.litote.kmongo.not
import org.litote.kmongo.pull
import org.litote.kmongo.push
import org.litote.kmongo.setValue
import java.util.UUID

internal class MongoUserStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : UserStore, MongoStore<UserEntity>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = UserEntity.name,
        clazz = UserEntity::class
    ),
    index = { ensureIndex(ascending(UserEntity::emailAddress), unique = true) }
) {

    override fun create(entity: UserEntity) {
        getByEmailAddress(entity.emailAddress)?.let { throw EmailAddressAlreadyTaken(entity.emailAddress) }
        collection.insertOne(entity)
    }

    override fun get(userId: UUID) = collection.findOneById(userId)

    override fun getByEmailAddress(emailAddress: String) = collection.findOne(UserEntity::emailAddress eq emailAddress)

    override fun update(userId: UUID, update: UserEntity.Update): UserEntity {
        return collection.findOneByIdAndUpdate(
            id = userId,
            update = combine(mutableListOf<Bson>().apply {
                update.firstName?.let { add(setValue(UserEntity.Update::firstName, it)) }
                update.lastName?.let { add(setValue(UserEntity.Update::lastName, it)) }
            })
        ) ?: throw UserNotFound()
    }

    override fun addRole(userId: UUID, role: JwtRole): UserEntity {
        get(userId) ?: throw UserNotFound()
        return collection.findOneAndUpdate(
            filter = and(UserEntity::id eq userId, not(UserEntity::roles contains role)),
            update = push(UserEntity::roles, role)
        ) ?: throw UserAlreadyHasRole(role)
    }

    override fun removeRole(userId: UUID, role: JwtRole): UserEntity {
        get(userId) ?: throw UserNotFound()
        return collection.findOneAndUpdate(
            filter = and(UserEntity::id eq userId, UserEntity::roles contains role),
            update = pull(UserEntity::roles, role)
        ) ?: throw UserDoesNotHaveRole(role)
    }

    override fun delete(userId: UUID) {
        collection.findOneByIdAndDelete(userId) ?: throw UserNotFound()
    }
}

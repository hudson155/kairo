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
import io.limberapp.backend.module.users.mapper.app.user.UserMapper
import io.limberapp.backend.module.users.model.user.UserModel
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
    mongoDatabase: MongoDatabase,
    private val userMapper: UserMapper
) : UserStore, MongoStore<UserEntity>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = UserEntity.name,
        clazz = UserEntity::class
    ),
    index = { ensureIndex(ascending(UserEntity::emailAddress), unique = true) }
) {

    override fun create(model: UserModel) {
        val entity = userMapper.entity(model)
        getByEmailAddress(entity.emailAddress)?.let { throw EmailAddressAlreadyTaken(entity.emailAddress) }
        collection.insertOne(entity)
    }

    override fun get(userId: UUID): UserModel? {
        val entity = collection.findOneById(userId) ?: return null
        return userMapper.model(entity)
    }

    override fun getByEmailAddress(emailAddress: String): UserModel? {
        val entity = collection.findOne(UserEntity::emailAddress eq emailAddress) ?: return null
        return userMapper.model(entity)
    }

    override fun update(userId: UUID, update: UserModel.Update): UserModel {
        val updateEntity = userMapper.update(update)
        val entity = collection.findOneByIdAndUpdate(
            id = userId,
            update = combine(mutableListOf<Bson>().apply {
                updateEntity.firstName?.let { add(setValue(UserEntity.Update::firstName, it)) }
                updateEntity.lastName?.let { add(setValue(UserEntity.Update::lastName, it)) }
            })
        ) ?: throw UserNotFound()
        return userMapper.model(entity)
    }

    override fun addRole(userId: UUID, role: JwtRole): UserModel {
        get(userId) ?: throw UserNotFound()
        val entity = collection.findOneAndUpdate(
            filter = and(UserEntity::id eq userId, not(UserEntity::roles contains role)),
            update = push(UserEntity::roles, role)
        ) ?: throw UserAlreadyHasRole(role)
        return userMapper.model(entity)
    }

    override fun removeRole(userId: UUID, role: JwtRole): UserModel {
        get(userId) ?: throw UserNotFound()
        val entity = collection.findOneAndUpdate(
            filter = and(UserEntity::id eq userId, UserEntity::roles contains role),
            update = pull(UserEntity::roles, role)
        ) ?: throw UserDoesNotHaveRole(role)
        return userMapper.model(entity)
    }

    override fun delete(userId: UUID) {
        collection.findOneByIdAndDelete(userId) ?: throw UserNotFound()
    }
}

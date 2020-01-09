package io.limberapp.backend.module.users.service.user

import com.google.inject.Inject
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.mapper.app.user.UserMapper
import io.limberapp.backend.module.users.model.user.UserModel
import io.limberapp.backend.module.users.store.user.UserStore
import java.util.UUID

internal class UserServiceImpl @Inject constructor(
    private val userStore: UserStore,
    private val userMapper: UserMapper
) : UserService {

    override fun create(model: UserModel) {
        val entity = userMapper.entity(model)
        userStore.create(entity)
    }

    override fun get(userId: UUID): UserModel? {
        val entity = userStore.get(userId) ?: return null
        return userMapper.model(entity)
    }

    override fun getByEmailAddress(emailAddress: String): UserModel? {
        val entity = userStore.getByEmailAddress(emailAddress) ?: return null
        return userMapper.model(entity)
    }

    override fun update(userId: UUID, update: UserModel.Update): UserModel {
        val entity = userStore.update(userId, userMapper.update(update))
        return userMapper.model(entity)
    }

    override fun addRole(userId: UUID, roleName: JwtRole): UserModel {
        val entity = userStore.addRole(userId, roleName)
        return userMapper.model(entity)
    }

    override fun removeRole(userId: UUID, roleName: JwtRole): UserModel {
        val entity = userStore.removeRole(userId, roleName)
        return userMapper.model(entity)
    }

    override fun delete(userId: UUID) {
        userStore.delete(userId)
    }
}

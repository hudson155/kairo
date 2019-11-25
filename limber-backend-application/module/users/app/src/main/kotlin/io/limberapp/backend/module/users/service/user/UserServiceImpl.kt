package io.limberapp.backend.module.users.service.user

import com.google.inject.Inject
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.mapper.app.user.UserMapper
import io.limberapp.backend.module.users.model.user.UserModel
import io.limberapp.backend.module.users.store.user.UserStore
import com.piperframework.exception.ConflictException
import com.piperframework.exception.NotFoundException
import java.util.UUID

internal class UserServiceImpl @Inject constructor(
    private val userStore: UserStore,
    private val userMapper: UserMapper
) : UserService {

    override fun create(model: UserModel) {
        getByEmailAddress(model.emailAddress)?.let { throw ConflictException() }
        val entity = userMapper.entity(model)
        userStore.create(entity)
    }

    override fun get(id: UUID): UserModel? {
        val entity = userStore.get(id) ?: return null
        return userMapper.model(entity)
    }

    override fun getByEmailAddress(emailAddress: String): UserModel? {
        val entity = userStore.getByEmailAddress(emailAddress) ?: return null
        return userMapper.model(entity)
    }

    override fun update(id: UUID, update: UserModel.Update): UserModel {
        val entity = userStore.update(id, userMapper.update(update)) ?: throw NotFoundException()
        return userMapper.model(entity)
    }

    override fun addRole(userId: UUID, roleName: JwtRole) {
        userStore.get(userId) ?: throw NotFoundException()
        return userStore.addRole(userId, roleName) ?: throw ConflictException()
    }

    override fun removeRole(userId: UUID, roleName: JwtRole) {
        userStore.get(userId) ?: throw NotFoundException()
        return userStore.removeRole(userId, roleName) ?: throw ConflictException()
    }

    override fun delete(id: UUID) = userStore.delete(id) ?: throw NotFoundException()
}

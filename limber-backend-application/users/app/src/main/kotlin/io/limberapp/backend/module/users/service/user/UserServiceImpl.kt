package io.limberapp.backend.module.users.service.user

import com.google.inject.Inject
import io.limberapp.backend.module.users.mapper.app.user.UserMapper
import io.limberapp.backend.module.users.model.user.UserModel
import io.limberapp.backend.module.users.store.user.UserStore
import java.util.UUID

internal class UserServiceImpl @Inject constructor(
    private val userStore: UserStore,
    private val userMapper: UserMapper
) : UserService {

    override fun create(model: UserModel.Creation): UserModel.Complete {
        val entity = userMapper.entity(model)
        userStore.create(entity)
        return userMapper.completeModel(entity)
    }

    override fun get(id: UUID): UserModel.Complete? {
        val entity = userStore.get(id) ?: return null
        return userMapper.completeModel(entity)
    }

    override fun getByEmailAddress(emailAddress: String): UserModel.Complete? {
        val entity = userStore.getByEmailAddress(emailAddress) ?: return null
        return userMapper.completeModel(entity)
    }

    override fun update(id: UUID, model: UserModel.Update): UserModel.Complete {
        val update = userMapper.update(model)
        val entity = userStore.update(id, update)
        return userMapper.completeModel(entity)
    }

    override fun delete(id: UUID) = userStore.delete(id)
}

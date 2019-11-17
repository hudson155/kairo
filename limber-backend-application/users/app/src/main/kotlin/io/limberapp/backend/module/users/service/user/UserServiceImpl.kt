package io.limberapp.backend.module.users.service.user

import com.google.inject.Inject
import io.limberapp.backend.module.users.mapper.app.user.UserMapper
import io.limberapp.backend.module.users.model.user.UserModel
import io.limberapp.backend.module.users.store.user.UserStore
import io.limberapp.framework.store.create
import io.limberapp.framework.store.get
import io.limberapp.framework.store.update
import java.util.UUID

internal class UserServiceImpl @Inject constructor(
    private val userStore: UserStore,
    private val userMapper: UserMapper
) : UserService {

    override fun create(model: UserModel.Creation): UserModel.Complete {
        val creationEntity = userMapper.creationEntity(model)
        val completeEntity = userStore.create(creationEntity)
        return userMapper.completeModel(completeEntity)
    }

    override fun get(id: UUID): UserModel.Complete? {
        val completeEntity = userStore.get(id) ?: return null
        return userMapper.completeModel(completeEntity)
    }

    override fun getByEmailAddress(emailAddress: String): UserModel.Complete? {
        val completeEntity = userStore.getByEmailAddress(emailAddress) ?: return null
        return userMapper.completeModel(completeEntity)
    }

    override fun update(id: UUID, model: UserModel.Update): UserModel.Complete {
        val updateEntity = userMapper.updateEntity(model)
        val completeEntity = userStore.update(id, updateEntity)
        return userMapper.completeModel(completeEntity)
    }

    override fun delete(id: UUID) = userStore.delete(id)
}

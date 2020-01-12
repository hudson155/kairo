package io.limberapp.backend.module.users.service.user

import com.google.inject.Inject
import com.piperframework.module.annotation.Store
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.model.user.UserModel
import java.util.UUID

internal class UserServiceImpl @Inject constructor(
    @Store private val userStore: UserService
) : UserService {

    override fun create(model: UserModel) = userStore.create(model)

    override fun get(userId: UUID) = userStore.get(userId)

    override fun getByEmailAddress(emailAddress: String) = userStore.getByEmailAddress(emailAddress)

    override fun update(userId: UUID, update: UserModel.Update) = userStore.update(userId, update)

    override fun addRole(userId: UUID, role: JwtRole) = userStore.addRole(userId, role)

    override fun removeRole(userId: UUID, role: JwtRole) = userStore.removeRole(userId, role)

    override fun delete(userId: UUID) = userStore.delete(userId)
}

package io.limberapp.backend.module.users.service.user

import com.google.inject.Inject
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.model.user.UserModel
import io.limberapp.backend.module.users.store.user.UserStore
import java.util.UUID

internal class UserServiceImpl @Inject constructor(
    private val userStore: UserStore
) : UserService {

    override fun create(model: UserModel) = userStore.create(model)

    override fun get(userId: UUID) = userStore.get(userId)

    override fun getByEmailAddress(emailAddress: String) = userStore.getByEmailAddress(emailAddress)

    override fun update(userId: UUID, update: UserModel.Update) = userStore.update(userId, update)

    override fun addRole(userId: UUID, roleName: JwtRole) = userStore.addRole(userId, roleName)

    override fun removeRole(userId: UUID, roleName: JwtRole) = userStore.removeRole(userId, roleName)

    override fun delete(userId: UUID) = userStore.delete(userId)
}

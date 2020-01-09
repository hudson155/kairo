package io.limberapp.backend.module.users.service.user

import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.model.user.UserModel
import java.util.UUID

interface UserService {

    fun create(model: UserModel)

    fun get(userId: UUID): UserModel?

    fun getByEmailAddress(emailAddress: String): UserModel?

    fun update(userId: UUID, update: UserModel.Update): UserModel

    fun addRole(userId: UUID, roleName: JwtRole): UserModel

    fun removeRole(userId: UUID, roleName: JwtRole): UserModel

    fun delete(userId: UUID)
}

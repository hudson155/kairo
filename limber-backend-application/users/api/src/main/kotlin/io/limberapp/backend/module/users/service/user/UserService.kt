package io.limberapp.backend.module.users.service.user

import io.limberapp.backend.module.users.model.user.UserModel
import java.util.UUID

interface UserService {

    fun create(model: UserModel)

    fun get(id: UUID): UserModel?

    fun getByEmailAddress(emailAddress: String): UserModel?

    fun update(id: UUID, update: UserModel.Update): UserModel

    fun delete(id: UUID)
}

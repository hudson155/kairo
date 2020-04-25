package io.limberapp.backend.module.users.service.account

import io.limberapp.backend.module.users.model.account.UserModel
import java.util.UUID

interface UserService {
    fun create(model: UserModel)

    fun get(userId: UUID): UserModel?

    fun getByEmailAddress(emailAddress: String): UserModel?

    fun update(userId: UUID, update: UserModel.Update): UserModel

    fun delete(userId: UUID)
}

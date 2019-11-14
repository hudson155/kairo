package io.limberapp.backend.module.users.service.user

import io.limberapp.backend.module.users.model.user.UserModel
import java.util.UUID

internal interface UserService {

    fun create(model: UserModel.Creation): UserModel.Complete

    fun get(id: UUID): UserModel.Complete?

    fun update(id: UUID, model: UserModel.Update): UserModel.Complete

    fun delete(id: UUID)
}

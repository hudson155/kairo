package io.limberapp.backend.module.users.service.user

import io.limberapp.backend.module.users.entity.user.UserEntity
import java.util.UUID

interface UserService {

    fun create(entity: UserEntity.Creation): UserEntity.Complete

    fun get(id: UUID): UserEntity.Complete?

    fun update(id: UUID, entity: UserEntity.Update): UserEntity.Complete

    fun delete(id: UUID)
}

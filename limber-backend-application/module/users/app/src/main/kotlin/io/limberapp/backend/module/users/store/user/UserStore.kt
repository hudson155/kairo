package io.limberapp.backend.module.users.store.user

import com.piperframework.store.Store
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.entity.user.UserEntity
import java.util.UUID

internal interface UserStore : Store {

    fun create(entity: UserEntity)

    fun get(userId: UUID): UserEntity?

    fun getByEmailAddress(emailAddress: String): UserEntity?

    fun update(userId: UUID, update: UserEntity.Update): UserEntity

    fun addRole(userId: UUID, role: JwtRole): UserEntity

    fun removeRole(userId: UUID, role: JwtRole): UserEntity

    fun delete(userId: UUID)
}

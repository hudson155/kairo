package io.limberapp.backend.module.users.store.user

import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.entity.user.UserEntity
import io.limberapp.framework.store.Store
import java.util.UUID

internal interface UserStore : Store<UserEntity> {

    fun create(entity: UserEntity)

    fun get(id: UUID): UserEntity?

    fun update(id: UUID, update: UserEntity.Update): UserEntity?

    fun getByEmailAddress(emailAddress: String): UserEntity?

    fun addRole(userId: UUID, roleName: JwtRole): Unit?

    fun removeRole(userId: UUID, roleName: JwtRole): Unit?

    fun delete(id: UUID): Unit?
}

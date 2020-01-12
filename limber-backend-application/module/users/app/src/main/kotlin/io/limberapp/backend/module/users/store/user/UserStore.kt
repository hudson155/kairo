package io.limberapp.backend.module.users.store.user

import com.piperframework.store.Store
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.model.user.UserModel
import java.util.UUID

internal interface UserStore : Store {

    fun create(model: UserModel)

    fun get(userId: UUID): UserModel?

    fun getByEmailAddress(emailAddress: String): UserModel?

    fun update(userId: UUID, update: UserModel.Update): UserModel

    fun addRole(userId: UUID, role: JwtRole): UserModel

    fun removeRole(userId: UUID, role: JwtRole): UserModel

    fun delete(userId: UUID)
}

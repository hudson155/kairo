package io.limberapp.backend.module.users.service.account

import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.model.account.UserModel
import java.util.*

interface UserService {
  fun create(model: UserModel)

  fun get(userGuid: UUID): UserModel?

  fun getByEmailAddress(emailAddress: String): UserModel?

  fun getByOrgGuid(orgGuid: UUID): Set<UserModel>

  fun update(userGuid: UUID, update: UserModel.Update): UserModel

  fun deleteRole(userGuid: UUID, role: JwtRole)

  fun delete(userGuid: UUID)
}

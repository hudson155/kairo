package io.limberapp.backend.module.users.service.account

import io.limberapp.backend.module.users.model.account.UserModel
import java.util.*

interface UserService {
  fun create(model: UserModel): UserModel

  fun get(userGuid: UUID): UserModel?

  fun getByEmailAddress(orgGuid: UUID, emailAddress: String): UserModel?

  fun getByOrgGuid(orgGuid: UUID): Set<UserModel>

  fun update(userGuid: UUID, update: UserModel.Update): UserModel

  suspend fun delete(userGuid: UUID)
}

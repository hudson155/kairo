package io.limberapp.backend.module.users.service.account

import com.piperframework.finder.Finder
import io.limberapp.backend.LimberModule
import io.limberapp.backend.module.users.model.account.UserFinder
import io.limberapp.backend.module.users.model.account.UserModel
import java.util.*

@LimberModule.Users
interface UserService : Finder<UserModel, UserFinder> {
  fun create(model: UserModel): UserModel

  fun update(userGuid: UUID, update: UserModel.Update): UserModel

  @LimberModule.Orgs
  fun delete(userGuid: UUID)
}
